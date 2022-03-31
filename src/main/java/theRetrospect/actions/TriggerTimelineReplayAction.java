package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;
import theRetrospect.util.MinionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TriggerTimelineReplayAction extends AbstractGameAction {

    private final TimelineMinion minion;
    private final int replayCount;
    private final boolean consumeCards;

    /**
     * Create an action that triggers a timeline replay.
     *
     * @param minion       The timeline to trigger. Use null for a random timeline.
     * @param replayCount  How many times the replay should be triggered.
     * @param consumeCards Whether the replayed cards should disappear from the timeline.
     */
    public TriggerTimelineReplayAction(TimelineMinion minion, int replayCount, boolean consumeCards) {
        this.minion = minion;
        this.replayCount = replayCount;
        this.consumeCards = consumeCards;
    }

    /**
     * Create an action that triggers a timeline replay.
     *
     * @param minion The timeline to trigger. Use null for a random timeline.
     */
    public TriggerTimelineReplayAction(TimelineMinion minion, boolean consumeCards) {
        this(minion, 1, consumeCards);
    }

    @Override
    public void update() {
        TimelineMinion target = this.minion;
        if (target == null) {
            MonsterGroup monsters = MinionUtils.getMinions(AbstractDungeon.player);
            List<TimelineMinion> timelines = monsters.monsters.stream().filter(m -> m instanceof TimelineMinion).map(m -> (TimelineMinion) m).collect(Collectors.toList());
            if (timelines.size() <= 0) {
                this.isDone = true;
                return;
            }
            target = timelines.get(AbstractDungeon.cardRng.random(timelines.size() - 1));
        }

        Optional<TimerPower> timer = target.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        if (timer.isPresent()) {
            for (int i = 0; i < replayCount && target.cards.size() > 0; i++) {
                if (consumeCards)
                    timer.get().endOfTurnPlayCards();
                else
                    timer.get().extraReplay();
            }

        }
        this.isDone = true;
    }
}
