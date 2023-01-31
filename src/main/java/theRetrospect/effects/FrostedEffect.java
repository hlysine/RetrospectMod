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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;

public class FrostedEffect extends AbstractGameEffect {
    public static final float LONG_DURATION = 5f;
    public static final float SHORT_DURATION = 2f;
    private static final Logger logger = LogManager.getLogger(FrostedEffect.class);
    private static final ShaderProgram frostedShader;

    static {
        frostedShader = new ShaderProgram(
                Gdx.files.internal(RetrospectMod.makeShaderPath("frosted/frosted.vert")).readString(),
                Gdx.files.internal(RetrospectMod.makeShaderPath("frosted/frosted.frag")).readString()
        );

        if (frostedShader.getLog().length() != 0) {
            logger.log(frostedShader.isCompiled() ? Level.WARN : Level.ERROR, frostedShader.getLog());
        }
    }

    private final PostProcessor postProcessor;
    private final Vector2 center;
    private final Vector2 focus;
    private final float focusRadius;

    public FrostedEffect(Vector2 center, Vector2 focus, float focusRadius) {
        this.duration = this.startingDuration = Settings.FAST_MODE ? SHORT_DURATION : LONG_DURATION;
        this.center = center;
        this.focus = focus;
        this.focusRadius = focusRadius;
        this.postProcessor = new PostProcessor();
        AbstractDungeon.topLevelEffects.add(new BorderLongFlashEffect(RetrospectMod.RETROSPECT_COLOR.cpy()));
    }

    public FrostedEffect(Vector2 center) {
        this(center, null, 0);
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
            frostedShader.begin();
            frostedShader.setUniformf("res_x", Gdx.graphics.getWidth());
            frostedShader.setUniformf("res_y", Gdx.graphics.getHeight());
            frostedShader.setUniformf("rnd_factor", 0.05f);
            frostedShader.setUniformf("rnd_scale", 1f);
            if (focus != null) {
                frostedShader.setUniformf("focus", focus.x, focus.y);
                frostedShader.setUniformf("focus_radius", 0);
                frostedShader.setUniformf("focus_radius_2", focusRadius);
            } else {
                frostedShader.setUniformf("focus_radius", 0);
                frostedShader.setUniformf("focus_radius_2", 0);
            }
            frostedShader.end();
        }

        @Override
        public void postProcess(SpriteBatch sb, TextureRegion frameTexture, OrthographicCamera camera) {
            ShaderProgram oldShader = sb.getShader();

            frostedShader.begin();

            float bouncingProgress = 1 - Math.abs(duration - startingDuration / 2) / (startingDuration / 2);
            float stepProgress = Math.min(1, 1 - (duration - startingDuration / 2) / (startingDuration / 2));
            frostedShader.setUniformf("rnd_factor", Interpolation.pow2.apply(bouncingProgress) * 0.2f);
            if (focus != null) {
                float focusProgress = Interpolation.pow2In.apply(stepProgress);
                frostedShader.setUniformf("focus_radius", focusRadius * (0.5f + focusProgress * 0.5f));
                frostedShader.setUniformf("focus_radius_2", focusRadius + 50 + 100 * focusProgress);
            }
            frostedShader.end();

            sb.setShader(frostedShader);
            sb.draw(frameTexture, 0, 0);
            sb.setShader(oldShader);
        }
    }
}
