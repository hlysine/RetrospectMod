package theRetrospect.patches.timeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;
import theRetrospect.mechanics.timeline.EnergySwitch;

@SpirePatch(
        clz = EnergyPanel.class,
        method = "render"
)
public class VolatileEnergyEffectPatch {

    private static final Logger logger = LogManager.getLogger(VolatileEnergyEffectPatch.class);

    private static final ShaderProgram invertShader;

    static {
        invertShader = new ShaderProgram(
                Gdx.files.internal(RetrospectMod.makeShaderPath("invert/invert.vert")).readString(),
                Gdx.files.internal(RetrospectMod.makeShaderPath("invert/invert.frag")).readString()
        );

        if (invertShader.getLog().length() != 0) {
            logger.log(invertShader.isCompiled() ? Level.WARN : Level.ERROR, invertShader.getLog());
        }
    }

    private static ShaderProgram oldShader;

    public static void Prefix(EnergyPanel __instance, SpriteBatch sb) {
        if (EnergySwitch.getCurrentSource() == EnergySwitch.EnergySource.VOLATILE) {
            oldShader = sb.getShader();
            sb.setShader(invertShader);
        }
    }

    public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
        if (EnergySwitch.getCurrentSource() == EnergySwitch.EnergySource.VOLATILE) {
            sb.setShader(oldShader);
        }
    }
}
