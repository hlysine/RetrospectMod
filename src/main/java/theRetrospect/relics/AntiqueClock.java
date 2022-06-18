package theRetrospect.relics;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.powers.AntiqueClockPower;
import theRetrospect.util.TextureLoader;

import static theRetrospect.RetrospectMod.makeRelicOutlinePath;
import static theRetrospect.RetrospectMod.makeRelicPath;

@AutoAdd.Seen
public class AntiqueClock extends AbstractBaseRelic {

    public static final String ID = RetrospectMod.makeID(AntiqueClock.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("antique_clock.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("antique_clock.png"));

    private static final RelicTier TIER = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public AntiqueClock() {
        super(ID, IMG, OUTLINE, TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractPlayer player = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(player, player, new AntiqueClockPower(player, 1)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
