package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.timelineActions.CollapseAllTimelineAction;
import theRetrospect.util.TimelineUtils;

public class ConvergeAction extends AbstractGameAction {
    public ConvergeAction() {
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.ENERGY;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            int timelineCount = TimelineUtils.getTimelines(AbstractDungeon.player).size();
            addToTop(new DrawCardAction(AbstractDungeon.player, timelineCount));
            addToTop(new GainEnergyAction(timelineCount));
            addToTop(new CollapseAllTimelineAction());
        }

        tickDuration();
    }
}