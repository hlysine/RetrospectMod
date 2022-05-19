package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShowCardToBePlayedEffect extends AbstractGameEffect {
    private final AbstractCard cardToPlay;
    private static final float PADDING = 30.0F * Settings.scale;

    public ShowCardToBePlayedEffect(AbstractCard cardToPlay) {
        this.cardToPlay = cardToPlay;
        this.duration = Settings.FAST_MODE ? 0.5f : 1.5f;
        this.startingDuration = Settings.FAST_MODE ? 0.5f : 1.5f;
        this.cardToPlay.target_x = Settings.WIDTH / 2f - PADDING - AbstractCard.IMG_WIDTH;
        this.cardToPlay.target_y = Settings.HEIGHT / 2f;
        this.cardToPlay.targetDrawScale = 0.75f;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.cardToPlay.update();

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }


    public void render(SpriteBatch sb) {
        if (!this.isDone)
            this.cardToPlay.render(sb);
    }

    public void dispose() {
    }
}