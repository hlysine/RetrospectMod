package theRetrospect.mechanics.timetravel;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.powers.TimeLinkPower;
import theRetrospect.subscribers.StateChangeSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.MonsterUtils;

import java.util.Optional;
import java.util.stream.Collectors;

public class StateManager {
    public static CombatStateTree stateTree;

    public static int getActiveRound() {
        return stateTree.getActiveRound();
    }

    public static void reset() {
        stateTree = new CombatStateTree();
    }

    public static void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(() -> {
            stateTree.addNode(GameActionManager.turn, CombatState.extractState());
            onActiveNodeChanged();
        }));
    }

    public static void atEndOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(() -> {
            stateTree.getActiveNode().cardsPlayedManually.clear();
            stateTree.getActiveNode().cardsPlayedManually.addAll(CardUtils.cardsManuallyPlayedThisTurn);
        }));
    }

    /**
     * Travel to a previous round. Present game state is saved as a mid-state of the current round while the previous round is restored.
     *
     * @param rounds            Number of rounds to travel back.
     * @param cardToExclude     Card to exclude from the rewind. This is used to prevent the rewind card from being played again.
     * @param persistingMonster Monster to persist through the effect of {@link theRetrospect.powers.TimeLinkPower}.
     * @return A Linear Path with the origin at the rewind destination and target at the time before rewinding.
     */
    public static CombatStateTree.LinearPath rewindTime(int rounds, AbstractCard cardToExclude, AbstractMonster persistingMonster) {
        // Save current state before rewinding
        CombatState currentState = CombatState.extractState();
        stateTree.getActiveNode().midStates.add(currentState);
        stateTree.getActiveNode().cardsPlayedManually.clear();
        stateTree.getActiveNode().cardsPlayedManually.addAll(
                // Exclude the rewind card from the list of manually played cards
                // Note that the rewind card is not excluded from the list in the extracted state.
                // It is only excluded from the list of cards that will be playable through the timeline.
                CardUtils.cardsManuallyPlayedThisTurn.stream()
                        .filter(c -> c != cardToExclude)
                        .collect(Collectors.toList())
        );

        // Rewind to the destination
        CombatStateTree.Node destination = stateTree.getNodeForRound(stateTree.getActiveNode(), stateTree.getActiveRound() - rounds);
        CombatStateTree.LinearPath path;
        if (destination == null) {
            destination = stateTree.addRoot(stateTree.getActiveRound() - rounds, stateTree.getRoot(stateTree.getActiveNode()).baseState.copy(persistingMonster));
            destination.baseState.turn = destination.round;

            path = stateTree.createLinearPath(stateTree.getActiveNode(), currentState, rounds);

            stateTree.setActiveNode(destination);
        } else {
            path = stateTree.createLinearPath(stateTree.getActiveNode(), currentState, rounds);
            if (path.originNode.parent == null) {
                // Rewinding to exactly round 1
                stateTree.setActiveNode(stateTree.addRoot(path.originNode.round, path.originNode.baseState.copy(persistingMonster)));
            } else {
                // Rewinding to a later round
                stateTree.setActiveNode(path.originNode.parent);
                stateTree.addNode(path.originNode.round, path.originNode.baseState.copy(persistingMonster));
            }
        }

        stateTree.getActiveNode().baseState.restoreStateForRewind();

        if (persistingMonster != null) {
            // Remove the Time Link power from the persisting monster
            Optional<AbstractMonster> monster = AbstractDungeon.getMonsters().monsters.stream().filter(m -> MonsterUtils.isSameMonster(m, persistingMonster)).findFirst();
            monster.ifPresent(abstractMonster -> AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(abstractMonster, abstractMonster, TimeLinkPower.POWER_ID, rounds)));
        }

        onActiveNodeChanged();
        return path;
    }

    private static void onActiveNodeChanged() {
        for (AbstractMonster monster : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
            if (monster instanceof StateChangeSubscriber) {
                ((StateChangeSubscriber) monster).onActiveNodeChanged();
            }
        }
    }
}