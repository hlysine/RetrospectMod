package theRetrospect.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.powers.AtomicClockPower;
import theRetrospect.util.TextureLoader;

import static theRetrospect.RetrospectMod.makeRelicOutlinePath;
import static theRetrospect.RetrospectMod.makeRelicPath;

public class AtomicClock extends AbstractBaseRelic {

    public static final String ID = RetrospectMod.makeID(AtomicClock.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic2.png"));

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public AtomicClock() {
        super(ID, IMG, OUTLINE, TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractPlayer player = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(player, player, new AtomicClockPower(player, 1)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
