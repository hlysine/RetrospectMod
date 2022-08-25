package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.runmods.InTime;

import java.util.Arrays;

public class NoVfxDamagePatch {

    public static final Logger logger = LogManager.getLogger(InTime.class);

    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class DamageInfoAddFieldPatch {
        public static final SpireField<Boolean> noVisualEffect = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayerDamagePatch {
        private static final String[] effectClasses = {
                "com.megacrit.cardcrawl.vfx.combat.StrikeEffect",
                "com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect",
                "com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect",
        };

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(javassist.expr.NewExpr m) throws CannotCompileException {
                    if (Arrays.stream(effectClasses).anyMatch(c -> m.getClassName().equals(c))) {
                        m.replace("{ if (!theRetrospect.util.DamageInfoUtils.getNoVisualEffect(info)) { $_ = $proceed($$); } " +
                                "else { $_ = new theRetrospect.effects.NoEffect(); } }");
                    }
                }
            };
        }
    }

}
