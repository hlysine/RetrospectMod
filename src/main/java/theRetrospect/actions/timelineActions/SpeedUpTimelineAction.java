package theRetrospect.actions.timelineActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;

public class SpeedUpTimelineAction extends AbstractGameAction {
    private final TimelineMinion minion;
    private final int amount;

    public SpeedUpTimelineAction(TimelineMinion minion, int amount) {
        this.minion = minion;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmpowerEffect(minion.drawX, minion.drawY)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion, AbstractDungeon.player, new TimerPower(minion)));
        this.isDone = true;
    }
}
