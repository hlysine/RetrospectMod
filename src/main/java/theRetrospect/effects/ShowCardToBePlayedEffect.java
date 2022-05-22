package theRetrospect.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.util.CardUtils;

public class ShowCardToBePlayedEffect extends AbstractGameEffect {
    private final AbstractCard cardToPlay;
    private static final float PADDING = 22.5F * Settings.scale;

    public ShowCardToBePlayedEffect(AbstractCard cardToPlay) {
        this.cardToPlay = cardToPlay;
        AbstractGameEffect self = this;
        CardUtils.addFollowUpActionToTop(this.cardToPlay, new AbstractGameAction() {
            @Override
            public void update() {
                self.isDone = true;
                this.isDone = true;
            }
        });
    }

    public void update() {
        if (!(boolean) ReflectionHacks.getPrivate(this.cardToPlay, AbstractCard.class, "darken")) {
            this.cardToPlay.target_x = Settings.WIDTH / 2f - PADDING - AbstractCard.IMG_WIDTH;
            this.cardToPlay.target_y = Settings.HEIGHT / 2f;
            this.cardToPlay.targetDrawScale = 0.75f;
        }
        this.cardToPlay.update();
    }


    public void render(SpriteBatch sb) {
        if (!this.isDone)
            this.cardToPlay.render(sb);
    }

    public void dispose() {
    }
}