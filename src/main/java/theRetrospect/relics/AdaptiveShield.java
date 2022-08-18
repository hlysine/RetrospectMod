package theRetrospect.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;

public class AdaptiveShield extends AbstractBaseRelic {

    public static final String ID = RetrospectMod.makeID(AdaptiveShield.class.getSimpleName());

    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public AdaptiveShield() {
        super(ID, LANDING_SOUND);
    }

    @Override
    public void onVictory() {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.increaseMaxHp(1, true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
