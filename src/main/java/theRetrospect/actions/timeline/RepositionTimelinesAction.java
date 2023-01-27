package theRetrospect.actions.timeline;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.TimelineUtils;

public class RepositionTimelinesAction extends AbstractGameAction {

    @Override
    public void update() {
        TimelineUtils.repositionTimelines(AbstractDungeon.player);
        this.isDone = true;
    }
}
