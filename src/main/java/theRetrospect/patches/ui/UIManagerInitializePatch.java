package theRetrospect.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.ui.UIManager;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = SpirePatch.STATICINITIALIZER
)
public class UIManagerInitializePatch {
    public static void Postfix() {
        UIManager.initialize();
    }
}
