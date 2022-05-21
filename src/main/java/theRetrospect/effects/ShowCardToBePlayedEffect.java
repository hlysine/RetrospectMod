package theRetrospect.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.util.CardUtils;

public class ShowCardToBePlayedEffect extends AbstractGameEffect {
    private final AbstractCard cardToPlay;
    private static final float PADDING = 30.0F * Settings.scale;

    public ShowCardToBePlayedEffect(AbstractCard cardToPlay) {
        this.cardToPlay = cardToPlay;
        this.cardToPlay.target_x = Settings.WIDTH / 2f - PADDING - AbstractCard.IMG_WIDTH;
        this.cardToPlay.target_y = Settings.HEIGHT / 2f;
        this.cardToPlay.targetDrawScale = 0.75f;
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
        this.cardToPlay.update();
    }


    public void render(SpriteBatch sb) {
        if (!this.isDone)
            this.cardToPlay.render(sb);
    }

    public void dispose() {
    }
}