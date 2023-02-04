package theRetrospect.patches.timetravel;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "updateDeathAnimation"
)
public class MonsterNoDisposeOnDeathPatch {

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(javassist.expr.MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("dispose")) {
                    m.replace("{ theRetrospect.mechanics.timetravel.TimeManager.scheduleDisposable(this); }");
                }
            }
        };
    }

}
