package theRetrospect.effects;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;

public class ShockwaveEffect extends AbstractGameEffect {
    private static final Logger logger = LogManager.getLogger(ShockwaveEffect.class);
    private static final ShaderProgram shockwaveShader;

    static {
        shockwaveShader = new ShaderProgram(
                Gdx.files.internal(RetrospectMod.makeShaderPath("shockwave/shockwave.vert")).readString(),
                Gdx.files.internal(RetrospectMod.makeShaderPath("shockwave/shockwave.frag")).readString()
        );

        if (shockwaveShader.getLog().length() != 0) {
            logger.log(shockwaveShader.isCompiled() ? Level.WARN : Level.ERROR, shockwaveShader.getLog());
        }
    }

    private final PostProcessor postProcessor;
    private final Vector2 center;

    public ShockwaveEffect(Vector2 center) {
        this.duration = this.startingDuration = 0.5f;
        this.center = center;
        this.postProcessor = new PostProcessor();
        AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(RetrospectMod.RETROSPECT_COLOR.cpy()));
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {
            ScreenPostProcessorManager.addPostProcessor(postProcessor);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            ScreenPostProcessorManager.removePostProcessor(postProcessor);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }

    public class PostProcessor implements ScreenPostProcessor {

        public PostProcessor() {
            shockwaveShader.begin();
            shockwaveShader.setUniformf("center", center.x / Gdx.graphics.getWidth(), center.y / Gdx.graphics.getHeight());
            shockwaveShader.setUniformf("shockParams", 10.0f, 0.8f, 0.1f); // base, power, width
            shockwaveShader.end();
        }

        @Override
        public void postProcess(SpriteBatch sb, TextureRegion frameTexture, OrthographicCamera camera) {
            ShaderProgram oldShader = sb.getShader();

            shockwaveShader.begin();
            shockwaveShader.setUniformf("time", 1 - duration / startingDuration);
            shockwaveShader.end();

            sb.setShader(shockwaveShader);
            sb.draw(frameTexture, 0, 0);
            sb.setShader(oldShader);
        }
    }
}
