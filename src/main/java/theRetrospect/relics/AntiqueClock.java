package theRetrospect.relics;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.CustomQueueCardAction;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.cards.skills.Divert;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TextureLoader;

import static theRetrospect.RetrospectMod.makeRelicOutlinePath;
import static theRetrospect.RetrospectMod.makeRelicPath;

@AutoAdd.Seen
public class AntiqueClock extends AbstractBaseRelic implements EndOfTurnCardSubscriber {

    public static final String ID = RetrospectMod.makeID(AntiqueClock.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("antique_clock.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("antique_clock.png"));

    private static final RelicTier TIER = RelicTier.STARTER;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public AntiqueClock() {
        super(ID, IMG, OUTLINE, TIER, LANDING_SOUND);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard(Runnable next) {
        if (!this.grayscale) {
            flash();
            AbstractCard card = new Divert();
            card.applyPowers();
            card.purgeOnUse = true;
            card.current_x = card.target_x = AbstractDungeon.player.drawX;
            card.current_y = card.target_y = AbstractDungeon.player.drawY;
            CardUtils.addFollowUpActionToBottom(card, new RunnableAction(next), true, 0);
            addToBot(new ShowCardToBePlayedAction(card));
            addToBot(new CustomQueueCardAction(card, true, false, true));
            this.grayscale = true;
        } else {
            next.run();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
