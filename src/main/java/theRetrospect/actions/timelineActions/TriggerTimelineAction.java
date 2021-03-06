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

public class TriggerTimelineAction extends AbstractGameAction {

    private final TimelineMinion minion;
    private final int triggerCount;
    private final boolean consumeCards;
    private final AbstractGameAction followUpAction;

    /**
     * Create an action that triggers a timeline.
     *
     * @param minion       The timeline to trigger. Use null for a random timeline.
     * @param triggerCount How many times the timeline should be triggered.
     * @param consumeCards Whether the played cards should disappear from the timeline.
     */
    public TriggerTimelineAction(TimelineMinion minion, int triggerCount, boolean consumeCards, AbstractGameAction followUpAction) {
        this.minion = minion;
        this.triggerCount = triggerCount;
        this.consumeCards = consumeCards;
        this.followUpAction = followUpAction;
    }

    /**
     * Create an action that triggers a timeline.
     *
     * @param minion The timeline to trigger. Use null for a random timeline.
     */
    public TriggerTimelineAction(TimelineMinion minion, boolean consumeCards, AbstractGameAction followUpAction) {
        this(minion, 1, consumeCards, followUpAction);
    }

    @Override
    public void update() {
        TimelineMinion target = this.minion;
        if (target == null) target = TimelineUtils.getRandomTimeline(AbstractDungeon.player);
        if (target == null) {
            this.isDone = true;
            return;
        }

        Optional<TimerPower> timer = target.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        if (timer.isPresent()) {
            AtomicInteger remainingAmount = new AtomicInteger(triggerCount);
            TimelineMinion finalTarget = target;
            CallbackUtils.ForLoop(
                    () -> remainingAmount.get() > 0 && finalTarget.cards.size() > 0 && !finalTarget.isDead,
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
