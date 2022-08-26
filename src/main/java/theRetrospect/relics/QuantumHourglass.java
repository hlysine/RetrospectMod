package theRetrospect.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.EnergizedPower;
import theRetrospect.subscribers.TimelineCollapseSubscriber;

public class QuantumHourglass extends AbstractBaseRelic implements TimelineCollapseSubscriber {

    public static final String ID = RetrospectMod.makeID(QuantumHourglass.class.getSimpleName());

    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public QuantumHourglass() {
        super(ID, LANDING_SOUND);
    }

    @Override
    public void afterTimelineCollapse(TimelineMinion timeline) {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergizedPower(AbstractDungeon.player, 1)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
