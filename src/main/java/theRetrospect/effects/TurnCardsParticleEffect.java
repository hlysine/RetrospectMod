package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class TurnCardsParticleEffect extends AbstractGameEffect {
    private final float x;
    private final float y;
    private float scale;
    private final float targetScale;
    private static Texture img;
    private static float globalRotation = 0.0F;

    public TurnCardsParticleEffect(float x, float y) {
        if (img == null) {
            img = ImageMaster.FROST_ACTIVATE_VFX_1;
        }

        this.targetScale = MathUtils.random(0.5F, 0.7F) * Settings.scale;
        this.scale = 0.1f;
        this.color = new Color();
        this.color.a = 0.0F;
        this.color.g = MathUtils.random(0.7F, 0.9F);
        this.color.r = this.color.g - 0.4F;
        this.color.b = this.color.r - 0.2F;

        this.x = x;
        this.y = y;
        this.rotation = globalRotation;
        globalRotation = (globalRotation + 64.0F) % 360.0F;
        this.startingDuration = 2.0F;
        this.duration = this.startingDuration;
    }

    public void update() {
        this.scale = Interpolation.smooth.apply(this.targetScale, 0.4f, this.duration / this.startingDuration);
        this.color.a = this.duration / this.startingDuration;

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img,
                this.x, this.y,
                0.0F, 0.0F,
                50.0F, 72.0F,
                this.scale, this.scale,
                this.rotation,
                0, 0,
                64, 64,
                false, false
        );
    }

    public void dispose() {
    }
}
