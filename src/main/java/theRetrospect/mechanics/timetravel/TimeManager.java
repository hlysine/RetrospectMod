package theRetrospect.mechanics.timetravel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
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
import theRetrospect.effects.TimelinePeekEffect;
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

    /**
     * Contains all objects (tree nodes, monsters, combat states) that need to be disposed when combat ends.
     */
    private static final List<Disposable> disposables = new ArrayList<>();


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
            AbstractDungeon.topLevelEffects.add(new TimelinePeekEffect(
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
        logger.info("Disposing " + disposables.size() + " disposable objects while resetting time manager");
        disposables.forEach(Disposable::dispose);
        disposables.clear();
        timeTree = new TimeTree();
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

    /**
     * Travel to the same round in another timeline. Present game state is saved as a mid-state of the current round in the current timeline.
     *
     * @param minion            Timeline to travel to.
     * @param cardToExclude     Card to exclude from the shift. This is used to prevent the shift card from being recorded in the timeline.
     * @param persistingMonster Monster to persist through the effect of {@link theRetrospect.powers.TimeLinkPower}.
     * @return A Linear Path with the same origin as the destination timeline and the target at the time before shifting.
     */
    public static TimeTree.LinearPath shiftTime(TimelineMinion minion, AbstractCard cardToExclude, AbstractMonster persistingMonster) {
        logger.info("Start shifting time");
        long startTime = System.currentTimeMillis();

        TimeTree.LinearPath timeline = minion.timelinePath;

        // Save current state before rewinding
        CombatState currentState = CombatState.extractState();
        timeTree.getActiveNode().midStates.add(currentState);
        timeTree.getActiveNode().cardsPlayedManually.clear();
        timeTree.getActiveNode().cardsPlayedManually.addAll(
                // Exclude the shift card from the list of manually played cards
                // Note that the shift card is not excluded from the list in the extracted state.
                // It is only excluded from the list of cards that will be playable through the timeline.
                CardUtils.cardsManuallyPlayedThisTurn.stream()
                        .filter(c -> c != cardToExclude)
                        .collect(Collectors.toList())
        );

        // Shift to the destination
        TimeTree.Node destination = timeline.getNodeForRound(timeTree.getActiveRound());
        if (destination == null) {
            throw new IllegalArgumentException("The destination timeline does not contain the current round.");
        }
        TimeTree.Node newOrigin;
        if (timeline.originNode.isRecursiveParentOf(timeTree.getActiveNode())) {
            newOrigin = timeline.originNode;
        } else {
            newOrigin = timeTree.getNodeForRound(timeTree.getActiveNode(), timeline.originNode.round);
            if (newOrigin == null) {
                newOrigin = timeTree.getRoot(timeTree.getActiveNode());
            }
        }
        TimeTree.LinearPath path = timeTree.createLinearPath(timeTree.getActiveNode(), currentState, newOrigin);
        timeTree.setActiveNode(destination);

        // Erase future of destination timeline
        destination.children.forEach(TimeManager::scheduleDisposable);
        destination.children.clear();
        destination.midStates.forEach(TimeManager::scheduleDisposable);
        destination.midStates.clear();

        if (persistingMonster != null) {
            Optional<AbstractMonster> monster = timeTree.getActiveNode().baseState.monsters.monsters.stream()
                    .filter(m -> MonsterUtils.isSameMonster(m, persistingMonster))
                    .findFirst();
            monster.ifPresent(m -> {
                CloneUtils.copyMonsterStates(persistingMonster, m, timeTree.getActiveNode().baseState.monsters);
                CloneUtils.fixMonsterStates(m, timeTree.getActiveNode().baseState);
            });
        }

        timeTree.getActiveNode().baseState.restoreStateCompletely();

        if (persistingMonster != null) {
            // Remove the Time Link power from the persisting monster
            Optional<AbstractMonster> monster = AbstractDungeon.getMonsters().monsters.stream().filter(m -> MonsterUtils.isSameMonster(m, persistingMonster)).findFirst();
            monster.ifPresent(abstractMonster -> AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(abstractMonster, abstractMonster, TimeLinkPower.POWER_ID, 1)));
        }

        // Swap timelines before notifying minions
        minion.timelinePath = path;

        onActiveNodeChanged();

        logger.info("Shifting time took " + (System.currentTimeMillis() - startTime) + "ms");

        return path;
    }

    private static void onActiveNodeChanged() {
        for (AbstractMonster monster : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
            if (monster instanceof StateChangeSubscriber) {
                ((StateChangeSubscriber) monster).onActiveNodeChanged();
            }
        }
    }

    /**
     * Schedule an object to be disposed when combat ends.
     * This is used because some unmanaged resources (such as textures) are shared between instances of the same class.
     * So they can only be disposed when all instances can be disposed.
     *
     * @param disposable Object to be disposed.
     */
    @SuppressWarnings("unused") // Used by instrument patch to AbstractMonster.updateDeathTimer()
    public static void scheduleDisposable(AbstractMonster disposable) {
        scheduleDisposable(new DisposableMonster(disposable));
    }

    /**
     * Schedule an object to be disposed when combat ends.
     * This is used because some unmanaged resources (such as textures) are shared between instances of the same class.
     * So they can only be disposed when all instances can be disposed.
     *
     * @param disposable Object to be disposed.
     */
    public static void scheduleDisposable(Disposable disposable) {
        if (disposables.contains(disposable)) {
            return;
        }
        if (disposable instanceof DisposableProxy) {
            DisposableProxy proxy = (DisposableProxy) disposable;
            if (disposables.stream().anyMatch(d -> d instanceof DisposableProxy && ((DisposableProxy) d).getDisposable() == proxy.getDisposable())) {
                return;
            }
        }
        disposables.add(disposable);
    }

    public static abstract class DisposableProxy implements Disposable {
        public abstract Object getDisposable();
    }

    public static class DisposableMonster extends DisposableProxy {
        private final AbstractMonster disposable;

        public DisposableMonster(AbstractMonster disposable) {
            this.disposable = disposable;
        }

        @Override
        public Object getDisposable() {
            return disposable;
        }

        @Override
        public void dispose() {
            disposable.dispose();
        }
    }
}
