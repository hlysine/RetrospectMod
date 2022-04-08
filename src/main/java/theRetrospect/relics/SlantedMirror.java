package theRetrospect.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.TextureLoader;

import static theRetrospect.RetrospectMod.makeRelicOutlinePath;
import static theRetrospect.RetrospectMod.makeRelicPath;

public class SlantedMirror extends CustomRelic implements TimelineConstructSubscriber {

    public static final String ID = RetrospectMod.makeID(SlantedMirror.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic2.png"));

    private static final RelicTier TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public SlantedMirror() {
        super(ID, IMG, OUTLINE, TIER, LANDING_SOUND);
    }

    @Override
    public void onTimelineConstruct(TimelineMinion timeline) {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToTop(new GainBlockAction(AbstractDungeon.player, 10));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
