package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.minions.TimelineMinion;

public class TimelineAuraEffect extends AbstractGameEffect {
    public static boolean switcher = true;
    private final TextureAtlas.AtlasRegion img = ImageMaster.EXHAUST_L;

    private final float vY;

    public TimelineAuraEffect(TimelineMinion minion) {
        if (MathUtils.randomBoolean()) {
            this.color = new Color(MathUtils.random(0.6f, 0.7f), MathUtils.random(0.2f, 0.3f), MathUtils.random(0.9f, 1f), 0.0f);
        } else {
            this.color = new Color(MathUtils.random(0.5f, 0.55f), MathUtils.random(0.5f, 0.6f), MathUtils.random(0.7f, 0.8f), 0.0f);
        }

        this.x = minion.hb.cX + MathUtils.random(-minion.hb.width / 16.0F, minion.hb.width / 16.0F);
        this.y = minion.hb.cY + MathUtils.random(-minion.hb.height / 16.0F, minion.hb.height / 12.0F);

        this.x -= this.img.packedWidth / 2.0f;
        this.y -= this.img.packedHeight / 2.0f;

        this.scale *= 1.5;

        this.duration = 2f;

        switcher = !switcher;

        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0f);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0f, 40.0f);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0f, -40.0f);
        }
    }

    private float y;
    private float x;

    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.2f, 0.0f, this.duration - 1.0f);
        } else {
            this.color.a = Interpolation.fade.apply(0.0f, 0.2f, this.duration);
        }
        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
