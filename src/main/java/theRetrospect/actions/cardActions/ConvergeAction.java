package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.timelineActions.CollapseAllTimelineAction;
import theRetrospect.util.TimelineUtils;

public class ConvergeAction extends AbstractGameAction {
    private final int energyGain;
    private final int cardDraw;

    public ConvergeAction(int energyGain, int cardDraw) {
        this.energyGain = energyGain;
        this.cardDraw = cardDraw;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.ENERGY;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            int timelineCount = TimelineUtils.getTimelines(AbstractDungeon.player).size();
            addToTop(new DrawCardAction(AbstractDungeon.player, timelineCount * cardDraw));
            addToTop(new GainEnergyAction(timelineCount * energyGain));
            addToTop(new CollapseAllTimelineAction());
        }

        tickDuration();
    }
}