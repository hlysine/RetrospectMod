package theRetrospect.timetravel;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.subscribers.StateChangeSubscriber;
import theRetrospect.util.CardUtils;

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
     * @param rounds Number of rounds to travel back.
     * @return A Linear Path with the origin at the present time.
     */
    public static CombatStateTree.LinearPath rewindTime(int rounds) {
        // Save current state before rewinding
        CombatState currentState = CombatState.extractState();
        stateTree.getActiveNode().midStates.add(currentState);
        stateTree.getActiveNode().cardsPlayedManually.clear();
        stateTree.getActiveNode().cardsPlayedManually.addAll(CardUtils.cardsManuallyPlayedThisTurn);

        // Rewind to the parent node of destination
        CombatStateTree.LinearPath path = stateTree.createLinearPath(stateTree.getActiveNode(), currentState, rounds);
        stateTree.setActiveNode(path.originNode.parent); // todo: handle null, which means rewinding past round 1

        // Set a new node as destination
        stateTree.addNode(path.originNode.round, path.originNode.baseState.copy());
        stateTree.getActiveNode().baseState.restoreStateForRewind();
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
