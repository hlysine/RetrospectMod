package theRetrospect.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.SmartCollarPower;
import theRetrospect.subscribers.TimelineConstructSubscriber;

public class SmartCollar extends AbstractBaseRelic implements TimelineConstructSubscriber {

    public static final String ID = RetrospectMod.makeID(SmartCollar.class.getSimpleName());
    public static final int COUNT = 7;

    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public SmartCollar() {
        super(ID, LANDING_SOUND);
        this.counter = 0;
    }

    @Override
    public void afterTimelineConstruct(TimelineMinion timeline) {
        this.counter++;

        if (this.counter == COUNT) {
            this.counter = 0;
            flash();
            this.pulse = false;
        } else if (this.counter == COUNT - 1) {
            beginPulse();
            this.pulse = true;
            AbstractDungeon.player.hand.refreshHandLayout();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SmartCollarPower(AbstractDungeon.player, 1), 1, true));
        }
    }

    @Override
    public void atBattleStart() {
        if (this.counter == COUNT - 1) {
            beginPulse();
            this.pulse = true;
            AbstractDungeon.player.hand.refreshHandLayout();
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SmartCollarPower(AbstractDungeon.player, 1), 1, true));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
