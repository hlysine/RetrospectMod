package theRetrospect.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

public class TimelineCircleEffect extends AbstractGameEffect {
    private final TextureAtlas.AtlasRegion img;
    private final AbstractFriendlyMonster minion;

    public TimelineCircleEffect(AbstractFriendlyMonster minion) {
        this.minion = minion;
        this.img = ImageMaster.POWER_UP_1;

        this.scale = MathUtils.random(3, 3.5f);
        this.startingDuration = this.duration = 2;

        this.color = new Color(MathUtils.random(0.5f, 0.6f), 0.4f, MathUtils.random(0.7f, 0.9f), 0.4f);

        this.renderBehind = true;
    }

    public void update() {
        super.update();
        this.color.a = 0.4f * this.duration / this.startingDuration;
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(this.color);
            sb.draw(
                    this.img,
                    minion.hb.cX - this.img.packedWidth / 2f, minion.hb.cY - this.img.packedHeight / 2f,
                    this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F,
                    this.img.packedWidth, this.img.packedHeight,
                    this.scale * MathUtils.random(0.95F, 1.05F),
                    this.scale * MathUtils.random(0.95F, 1.05F),
                    this.rotation
            );
        }
    }

    public void dispose() {
    }
}