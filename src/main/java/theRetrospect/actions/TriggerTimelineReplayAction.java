package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;

import java.util.Optional;

public class TriggerTimelineReplayAction extends AbstractGameAction {

    private final TimelineMinion minion;
    private final int replayCount;

    public TriggerTimelineReplayAction(TimelineMinion minion) {
        this(minion, 1);
    }

    public TriggerTimelineReplayAction(TimelineMinion minion, int replayCount) {
        this.minion = minion;
        this.replayCount = replayCount;
    }

    @Override
    public void update() {
        Optional<TimerPower> timer = minion.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        if (timer.isPresent()) {
            for (int i = 0; i < replayCount; i++)
                timer.get().extraReplay();
        }
        this.isDone = true;
    }
}
