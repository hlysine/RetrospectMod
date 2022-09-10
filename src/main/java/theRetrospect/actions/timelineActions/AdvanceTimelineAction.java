package theRetrospect.actions.timelineActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.TimelineUtils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvanceTimelineAction extends AbstractGameAction {

    private final TimelineMinion minion;
    private final int triggerCount;
    private final boolean consumeCards;
    private final AbstractGameAction followUpAction;

    /**
     * Create an action that advances a timeline.
     *
     * @param minion       The timeline to trigger. Use null for a random timeline.
     * @param advanceCount How many times the timeline should be advanced.
     * @param consumeCards Whether the played cards should disappear from the timeline.
     */
    public AdvanceTimelineAction(TimelineMinion minion, int advanceCount, boolean consumeCards, AbstractGameAction followUpAction) {
        this.minion = minion;
        this.triggerCount = advanceCount;
        this.consumeCards = consumeCards;
        this.followUpAction = followUpAction;
    }

    /**
     * Create an action that advances a timeline.
     *
     * @param minion The timeline to advance. Use null for a random timeline.
     */
    public AdvanceTimelineAction(TimelineMinion minion, boolean consumeCards, AbstractGameAction followUpAction) {
        this(minion, 1, consumeCards, followUpAction);
    }

    @Override
    public void update() {
        TimelineMinion target = this.minion;
        if (target == null) target = TimelineUtils.getRandomTimeline(AbstractDungeon.player, t -> t.cards.size() > 0);
        if (target == null) {
            this.isDone = true;
            return;
        }

        Optional<TimerPower> timer = target.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        if (timer.isPresent()) {
            AtomicInteger remainingAmount = new AtomicInteger(triggerCount);
            TimelineMinion finalTarget = target;
            CallbackUtils.ForLoop(
                    () -> remainingAmount.get() > 0 && finalTarget.cards.size() > 0 && !finalTarget.isDeadOrEscaped(),
                    () -> {
                        int i = remainingAmount.decrementAndGet();
                        if (i > 0)
                            addToBot(new WaitAction(0.75f));
                    },
                    next -> {
                        finalTarget.inTurn = true;
                        if (consumeCards)
                            timer.get().triggerOnEndOfTurnForPlayingCard(next);
                        else
                            timer.get().triggerWithoutConsumingCards(next);
                    },
                    () -> {
                        finalTarget.inTurn = false;
                        addToBot(followUpAction);
                    }
            );
        }
        this.isDone = true;
    }
}