package theRetrospect.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;

public class ShatteredMirror extends AbstractBaseRelic implements TimelineConstructSubscriber {

    public static final String ID = RetrospectMod.makeID(ShatteredMirror.class.getSimpleName());

    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public ShatteredMirror() {
        super(ID, LANDING_SOUND);
    }

    @Override
    public void afterTimelineConstruct(TimelineMinion timeline) {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToTop(new GainBlockAction(AbstractDungeon.player, 5));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
