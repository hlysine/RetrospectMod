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
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;

public class TimeTravelEffect extends AbstractGameEffect {
    public static final float LONG_DURATION = 5f;
    public static final float SHORT_DURATION = 2f;
    private static final Logger logger = LogManager.getLogger(TimeTravelEffect.class);
    private static final ShaderProgram whirlShader;
    private static final Interpolation swingIn1 = new Interpolation.SwingIn(2f);
    private static final Interpolation swingIn2 = new Interpolation.SwingIn(1f);

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
    private final Vector2 focus;
    private final float focusRadius;

    public TimeTravelEffect(Vector2 center, Vector2 focus, float focusRadius) {
        this.duration = this.startingDuration = Settings.FAST_MODE ? SHORT_DURATION : LONG_DURATION;
        this.center = center;
        this.focus = focus;
        this.focusRadius = focusRadius;
        this.postProcessor = new PostProcessor();
        AbstractDungeon.topLevelEffects.add(new BorderLongFlashEffect(RetrospectMod.RETROSPECT_COLOR.cpy()));
    }

    public TimeTravelEffect(Vector2 center) {
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
            AbstractDungeon.actionManager.addToBottom(new VFXAction(AbstractDungeon.player, new PlayerTurnWithoutEnergyEffect(), PlayerTurnWithoutEnergyEffect.DUR, true));
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
            if (focus != null) {
                whirlShader.setUniformf("focus", focus.x, focus.y);
                whirlShader.setUniformf("focus_radius", 0);
                whirlShader.setUniformf("focus_radius_2", focusRadius);
            } else {
                whirlShader.setUniformf("focus_radius", 0);
                whirlShader.setUniformf("focus_radius_2", 0);
            }
            whirlShader.end();

            diagRadius = (float) Math.sqrt(Math.pow(Gdx.graphics.getWidth(), 2) + Math.pow(Gdx.graphics.getHeight(), 2));
        }

        @Override
        public void postProcess(SpriteBatch sb, TextureRegion frameTexture, OrthographicCamera camera) {
            ShaderProgram oldShader = sb.getShader();

            whirlShader.begin();

            float bouncingProgress = 1 - Math.abs(duration - startingDuration / 2) / (startingDuration / 2);
            float stepProgress = Math.min(1, 1 - (duration - startingDuration / 2) / (startingDuration / 2));
            whirlShader.setUniformf("angle", 2f * (duration > startingDuration / 2 ? swingIn1 : swingIn2).apply(bouncingProgress));
            whirlShader.setUniformf("radius", diagRadius * Interpolation.pow2.apply(stepProgress));
            if (focus != null) {
                float focusProgress = Interpolation.pow2In.apply(stepProgress);
                whirlShader.setUniformf("focus_radius", focusRadius * (0.2f + focusProgress * 0.8f));
                whirlShader.setUniformf("focus_radius_2", focusRadius + 200 + 300 * focusProgress);
            }
            whirlShader.end();

            sb.setShader(whirlShader);
            sb.draw(frameTexture, 0, 0);
            sb.setShader(oldShader);
        }
    }
}
