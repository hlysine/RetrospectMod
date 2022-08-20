package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

public class TimelineCollapseParticleEffect extends AbstractGameEffect {
    private final float dur_div2;

    private float vX;
    private float vY;
    private float x;
    private float y;

    private final float dvx;
    private final float dvy;

    public TimelineCollapseParticleEffect(AbstractFriendlyMonster minion) {
        this.duration = MathUtils.random(1.3F, 1.8F);
        this.color = new Color(MathUtils.random(0.6f, 0.7f), MathUtils.random(0.3f, 0.4f), MathUtils.random(0.7f, 0.8f), 0.0f);

        dur_div2 = this.duration / 2.0F;

        vX = MathUtils.random(-300.0F, -50.0F) * Settings.scale;
        vY = MathUtils.random(-200.0F, -100.0F) * Settings.scale;
        x = minion.hb.cX + MathUtils.random(50, 80) * Settings.scale - 32.0F;
        y = minion.hb.cY + MathUtils.random(-25, 110) * Settings.scale - 32.0F;

        dvx = 400.0F * Settings.scale * this.scale;
        dvy = 100.0F * Settings.scale;
    }

    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.vY += Gdx.graphics.getDeltaTime() * this.dvy;
        this.vX -= Gdx.graphics.getDeltaTime() * this.dvx;
        //noinspection SuspiciousNameCombination
        this.rotation = -(57.295776F * MathUtils.atan2(this.vX, this.vY)) - 0.0F;

        if (this.duration > this.dur_div2) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - this.dur_div2) / this.dur_div2);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / this.dur_div2);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.FROST_ACTIVATE_VFX_1, this.x, this.y, 32.0F, 32.0F, 25.0F, 128.0F, this.scale, this.scale + (this.dur_div2 * 0.4F - this.duration) * Settings.scale, this.rotation, 0, 0, 64, 64, false, false);

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
