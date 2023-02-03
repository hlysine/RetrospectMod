package theRetrospect.patches.ui;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;

@SpirePatch(
        clz = CancelButton.class,
        method = SpirePatch.CONSTRUCTOR
)
public class RelocateCancelButtonPatch {
    public static void Postfix(CancelButton __instance) {
        // move the cancel button up so that it doesn't cover the energy panel
        ReflectionHacks.setPrivateStaticFinal(CancelButton.class, "DRAW_Y", 256.0F * Settings.scale);
        __instance.hb.moveY(256.0F * Settings.scale + 60.0F * Settings.scale);
    }
}
