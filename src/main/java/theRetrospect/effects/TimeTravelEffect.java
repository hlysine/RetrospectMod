package theRetrospect.effects;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;

public class TimeTravelEffect extends AbstractGameEffect {
    private static final float DURATION = 1.5f;
    private static final Logger logger = LogManager.getLogger(TimeTravelEffect.class);
    private static final ShaderProgram whirlShader;

    static {
        whirlShader = new ShaderProgram(
                Gdx.files.internal(RetrospectMod.makeShaderPath("whirl/whirl.vert")).readString(),
                Gdx.files.internal(RetrospectMod.makeShaderPath("whirl/whirl.frag")).readString()
        );

        if (whirlShader.getLog().length() != 0) {
            logger.log(whirlShader.isCompiled() ? Level.WARN : Level.ERROR, whirlShader.getLog());
        }
    }

    private final PostProcessor postProcessor;
    private final Vector2 center;

    public TimeTravelEffect(Vector2 center) {
        this.duration = DURATION;
        this.startingDuration = DURATION;
        this.center = center;
        this.postProcessor = new PostProcessor();
        AbstractDungeon.topLevelEffects.add(new BorderLongFlashEffect(RetrospectMod.RETROSPECT_COLOR.cpy()));
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
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
        private final float diagRadius;

        public PostProcessor() {
            whirlShader.begin();
            whirlShader.setUniformf("res_x", Gdx.graphics.getWidth());
            whirlShader.setUniformf("res_y", Gdx.graphics.getHeight());
            whirlShader.setUniformf("center", center.x, center.y);
            whirlShader.end();

            diagRadius = (float) Math.sqrt(Math.pow(Gdx.graphics.getWidth(), 2) + Math.pow(Gdx.graphics.getHeight(), 2));
        }

        @Override
        public void postProcess(SpriteBatch sb, TextureRegion frameTexture, OrthographicCamera camera) {
            ShaderProgram oldShader = sb.getShader();

            whirlShader.begin();
            float progress = 1 - Math.abs(duration - DURATION / 2) / (DURATION / 2);
            progress = Interpolation.pow2.apply(progress);
            whirlShader.setUniformf("radius", diagRadius * progress);
            whirlShader.setUniformf("angle", progress * 1.2f);
            whirlShader.end();

            sb.setShader(whirlShader);
            sb.draw(frameTexture, 0, 0);
            sb.setShader(oldShader);
        }
    }
}
