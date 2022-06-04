package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineCollapseSubscriber;

/**
 * Trigger afterTimelineCollapse for player powers and relics
 */
public class TriggerAfterTimelineCollapseAction extends AbstractGameAction {
    private final TimelineMinion minion;

    public TriggerAfterTimelineCollapseAction(TimelineMinion minion) {
        this.minion = minion;
    }

    public void update() {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) power;
                listener.afterTimelineCollapse(minion);
            }
        }

        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) relic;
                listener.afterTimelineCollapse(minion);
            }
        }
        this.isDone = true;
    }
}