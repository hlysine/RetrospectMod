package theRetrospect.actions.timeline;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineUtils;

public class CollapseAllTimelineAction extends AbstractGameAction {

    @Override
    public void update() {
        for (TimelineMinion timeline : TimelineUtils.getTimelines(AbstractDungeon.player)) {
            addToTop(new CollapseTimelineAction(timeline));
        }
        this.isDone = true;
    }
}
