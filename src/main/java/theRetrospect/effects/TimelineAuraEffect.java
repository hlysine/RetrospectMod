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
    private final TimelineMinion minion;

    private float dx;
    private float dy;
    private final float vY;
    private boolean wasInTurn;

    public TimelineAuraEffect(TimelineMinion minion) {
        this.minion = minion;
        this.wasInTurn = minion.inTurn;
        this.changeColor();

        this.dx = MathUtils.random(-minion.hb.width / 16.0F, minion.hb.width / 16.0F);
        this.dy = MathUtils.random(-minion.hb.height / 16.0F, minion.hb.height / 16.0F);

        this.dx -= this.img.packedWidth / 2.0f;
        this.dy -= this.img.packedHeight / 2.0f;

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

    public void update() {
        if (minion.inTurn != this.wasInTurn) {
            this.changeColor();
            this.wasInTurn = minion.inTurn;
        }
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
        sb.draw(
                this.img,
                minion.hb.cX + this.dx, minion.hb.cY + this.dy,
                this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f,
                this.img.packedWidth, this.img.packedHeight,
                this.scale, this.scale,
                this.rotation
        );
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }

    private void changeColor() {
        if (minion.inTurn) {
            this.color = new Color(MathUtils.random(0.7f, 0.9f), MathUtils.random(0.8f, 1.0f), 0.2f, this.color == null ? 0.0f : this.color.a);
        } else if (minion.isDeadOrEscaped()) {
            this.color = new Color(MathUtils.random(0.5f, 0.7f), MathUtils.random(0.5f, 0.7f), MathUtils.random(0.5f, 0.7f), this.color == null ? 0.0f : this.color.a);
        } else if (MathUtils.randomBoolean()) {
            this.color = new Color(MathUtils.random(0.6f, 0.7f), MathUtils.random(0.2f, 0.3f), MathUtils.random(0.9f, 1f), this.color == null ? 0.0f : this.color.a);
        } else {
            this.color = new Color(MathUtils.random(0.5f, 0.55f), MathUtils.random(0.5f, 0.6f), MathUtils.random(0.7f, 0.8f), this.color == null ? 0.0f : this.color.a);
        }
    }
}
