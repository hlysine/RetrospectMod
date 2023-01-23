package theRetrospect.effects;

import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;

public class TimeTravelEffect extends AbstractGameEffect {
    private static final float DURATION = 1f;
    private final PostProcessor postProcessor = new PostProcessor();

    public TimeTravelEffect() {
        this.duration = DURATION;
        this.startingDuration = DURATION;
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
        postProcessor.dispose();
    }

    public static class PostProcessor implements ScreenPostProcessor, Disposable {
        private static final Logger logger = LogManager.getLogger(PostProcessor.class);
        private static final ShaderProgram blurShader;

        private final FrameBuffer blurTargetA;
        private final FrameBuffer blurTargetB;
        private final TextureRegion fboRegion;

        static {
            blurShader = new ShaderProgram(
                    Gdx.files.internal(RetrospectMod.makeShaderPath("blur/vert.vs")).readString(),
                    Gdx.files.internal(RetrospectMod.makeShaderPath("blur/frag.fs")).readString()
            );

            if (blurShader.getLog().length() != 0) {
                logger.log(blurShader.isCompiled() ? Level.WARN : Level.ERROR, blurShader.getLog());
            }
        }

        public PostProcessor() {
            blurShader.begin();
            blurShader.setUniformf("dir", 0f, 0f);
            blurShader.setUniformf("resolution_x", Gdx.graphics.getWidth());
            blurShader.setUniformf("resolution_y", Gdx.graphics.getHeight());
            blurShader.setUniformf("radius", 2f);
            blurShader.end();

            blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
            blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
            fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
            //fboRegion.flip(false, true);
        }

        @Override
        public void postProcess(SpriteBatch sb, TextureRegion frameTexture, OrthographicCamera camera) {
            ShaderProgram oldShader = sb.getShader();
            int srcBlend = sb.getBlendSrcFunc();
            int dstBlend = sb.getBlendDstFunc();
            sb.setColor(Color.WHITE);
            sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
            sb.draw(frameTexture, Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.1f, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0.8f, 0.8f, 0f);
//            sb.end();
//
//            //Bind FBO target A
//            blurTargetA.begin();
//
//            //Clear FBO A with an opaque colour to minimize blending issues
//            Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            sb.setProjectionMatrix(camera.combined);
//
//            //now we can start our batch
//            sb.begin();
//
//            //render our scene fully to FBO A
//            sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
//            sb.draw(frameTexture, 0, 0);
//
//            //flush the batch, i.e. render entities to GPU
//            sb.flush();
//
//            //After flushing, we can finish rendering to FBO target A
//            blurTargetA.end();
//
//            //swap the shaders
//            //this will send the batch's (FBO-sized) projection matrix to our blur shader
//            sb.setShader(blurShader);
//
//            //ensure the direction is along the X-axis only
//            blurShader.setUniformf("dir", 1f, 0f);
//            blurShader.setUniformf("radius", 2f);
//
//            //start rendering to target B
//            blurTargetB.begin();
//
//            fboRegion.setTexture(blurTargetA.getColorBufferTexture());
//
//            //render target A (the scene) using our horizontal blur shader
//            //it will be placed into target B
//            sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
//            sb.draw(fboRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//            //flush the batch before ending target B
//            sb.flush();
//
//            //finish rendering target B
//            blurTargetB.end();
//
//            //now we can render to the screen using the vertical blur shader
//            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            sb.setProjectionMatrix(camera.combined);
//
//            //apply the blur only along Y-axis
//            blurShader.setUniformf("dir", 0f, 1f);
//            blurShader.setUniformf("radius", 2f);
//
//            fboRegion.setTexture(blurTargetB.getColorBufferTexture());
//
//            //draw the horizontally-blurred FBO target B to the screen, applying the vertical blur as we go
//            sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
//            sb.draw(fboRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            sb.setShader(oldShader);
            sb.setBlendFunction(srcBlend, dstBlend);
        }

        @Override
        public void dispose() {
            blurTargetA.dispose();
            blurTargetB.dispose();
        }
    }
}
