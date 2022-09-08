package theRetrospect.effects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.util.TextureLoader;

public class UnceasingEndTurnEffect extends AbstractGameEffect {
    private final float x;
    private static TextureAtlas.AtlasRegion img = null;
    private float y;
    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/timer84.png");

    public UnceasingEndTurnEffect() {
        if (img == null)
            img = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.startingDuration = 1.0F;
        this.duration = this.startingDuration;
        this.scale = Settings.scale * 3.0F;
        this.x = Settings.WIDTH * 0.5F - img.packedWidth / 2.0F;
        this.y = img.packedHeight / 2.0F;
        this.color = Color.WHITE.cpy();
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.duration < 0.5F) {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        }
        this.y = Interpolation.swing.apply(-img.packedHeight / 2.0F, Settings.HEIGHT * 0.7F - img.packedHeight / 2.0F, this.duration);
    }


    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, this.scale, this.scale, this.duration * 360.0F);
    }

    public void dispose() {
    }
}