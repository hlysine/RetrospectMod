package theRetrospect.mechanics.timetravel;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import hlysine.friendlymonsters.utils.MinionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.effects.PeekFrostedEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimeLinkPower;
import theRetrospect.subscribers.StateChangeSubscriber;
import theRetrospect.ui.UIManager;
import theRetrospect.util.CardUtils;
import theRetrospect.util.CloneUtils;
import theRetrospect.util.MonsterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TimeManager {
    private static final Logger logger = LogManager.getLogger(TimeManager.class);
    /**
     * Contains all dead monsters that need to be disposed when combat ends.
     */
    private static final List<AbstractMonster> pendingDispose = new ArrayList<>();


    /**
     * The time tree of the current combat, storing {@link CombatState}s at each round in each timeline.
     */
    public static TimeTree timeTree;
    /**
     * The RNG used to decide unstable outcomes. This is the only RNG not affected by any form of time travel.
     */
    public static Random unstableRng;
    /**
     * The timeline that is being peeked.
     */
    public static TimelineMinion peekMinion;
    /**
     * The state of the combat before peeking.
     */
    public static CombatState savedState;


    public static void atStartOfTurn() {
        TimeTree.Node newNode = timeTree.addNode(GameActionManager.turn, null);
        onActiveNodeChanged();
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(() -> {
            newNode.baseState = CombatState.extractState();
        }));
    }

    public static void atEndOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(() -> {
            timeTree.getActiveNode().cardsPlayedManually.clear();
            timeTree.getActiveNode().cardsPlayedManually.addAll(CardUtils.cardsManuallyPlayedThisTurn);
        }));
    }

    public static void onPeekStatusChanged() {
        if (AbstractDungeon.screen == UIManager.TIMELINE_CARDS_VIEW) {
            AbstractDungeon.topLevelEffects.add(new PeekFrostedEffect(
                    new Vector2(peekMinion.hb.cX, peekMinion.hb.cY),
                    Math.max(peekMinion.hb.height, peekMinion.hb.width) / 2
            ));
            if (PeekButton.isPeeking) {
                savedState = CombatState.extractState();
                peekMinion.getCurrentNode().baseState.restoreStateCompletely();
            } else if (savedState != null) {
                savedState.restoreStateCompletely();
                savedState = null;
            }
        }
    }


    public static int getActiveRound() {
        return timeTree.getActiveRound();
    }

    public static void reset() {
        if (timeTree != null) {
            timeTree.dispose();
        }
        pendingDispose.forEach(AbstractMonster::dispose);
        pendingDispose.clear();
        timeTree = new TimeTree();
    }

    /**
     * Schedule a {@link AbstractMonster} to be disposed when combat ends.
     * This is used because the monster shares textures with other monsters of the same type,
     * so they can only be disposed when all instances are dead.
     *
     * @param disposable Monster to be disposed.
     */
    @SuppressWarnings("unused") // Used by instrument patch to AbstractMonster.updateDeathTimer()
    public static void scheduleDisposable(AbstractMonster disposable) {
        if (pendingDispose.contains(disposable)) {
            return;
        }
        pendingDispose.add(disposable);
    }

    /**
     * Travel to a previous round. Present game state is saved as a mid-state of the current round while the previous round is restored.
     *
     * @param rounds            Number of rounds to travel back.
     * @param cardToExclude     Card to exclude from the rewind. This is used to prevent the rewind card from being played again.
     * @param persistingMonster Monster to persist through the effect of {@link theRetrospect.powers.TimeLinkPower}.
     * @return A Linear Path with the origin at the rewind destination and target at the time before rewinding.
     */
    public static TimeTree.LinearPath rewindTime(int rounds, AbstractCard cardToExclude, AbstractMonster persistingMonster) {
        logger.info("Start rewinding time");
        long startTime = System.currentTimeMillis();

        // Save current state before rewinding
        CombatState currentState = CombatState.extractState();
        timeTree.getActiveNode().midStates.add(currentState);
        timeTree.getActiveNode().cardsPlayedManually.clear();
        timeTree.getActiveNode().cardsPlayedManually.addAll(
                // Exclude the rewind card from the list of manually played cards
                // Note that the rewind card is not excluded from the list in the extracted state.
                // It is only excluded from the list of cards that will be playable through the timeline.
                CardUtils.cardsManuallyPlayedThisTurn.stream()
                        .filter(c -> c != cardToExclude)
                        .collect(Collectors.toList())
        );

        // Rewind to the destination
        TimeTree.Node destination = timeTree.getNodeForRound(timeTree.getActiveNode(), timeTree.getActiveRound() - rounds);
        TimeTree.LinearPath path;
        if (destination == null) {
            destination = timeTree.addRoot(timeTree.getActiveRound() - rounds, timeTree.getRoot(timeTree.getActiveNode()).baseState.copy());
            destination.baseState.turn = destination.round;

            path = timeTree.createLinearPath(timeTree.getActiveNode(), currentState, rounds);

            timeTree.setActiveNode(destination);
        } else {
            path = timeTree.createLinearPath(timeTree.getActiveNode(), currentState, rounds);
            if (path.originNode.parent == null) {
                // Rewinding to exactly round 1
                timeTree.setActiveNode(timeTree.addRoot(path.originNode.round, path.originNode.baseState.copy()));
            } else {
                // Rewinding to a later round
                timeTree.setActiveNode(path.originNode.parent);
                timeTree.addNode(path.originNode.round, path.originNode.baseState.copy());
            }
        }

        if (persistingMonster != null) {
            Optional<AbstractMonster> monster = timeTree.getActiveNode().baseState.monsters.monsters.stream()
                    .filter(m -> MonsterUtils.isSameMonster(m, persistingMonster))
                    .findFirst();
            monster.ifPresent(m -> {
                CloneUtils.copyMonsterStates(persistingMonster, m, timeTree.getActiveNode().baseState.monsters);
                CloneUtils.fixMonsterStates(m, timeTree.getActiveNode().baseState);
            });
        }

        timeTree.getActiveNode().baseState.restoreStateForRewind();

        if (persistingMonster != null) {
            // Remove the Time Link power from the persisting monster
            Optional<AbstractMonster> monster = AbstractDungeon.getMonsters().monsters.stream().filter(m -> MonsterUtils.isSameMonster(m, persistingMonster)).findFirst();
            monster.ifPresent(abstractMonster -> AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(abstractMonster, abstractMonster, TimeLinkPower.POWER_ID, rounds)));
        }

        onActiveNodeChanged();

        logger.info("Rewinding time took " + (System.currentTimeMillis() - startTime) + "ms");

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
