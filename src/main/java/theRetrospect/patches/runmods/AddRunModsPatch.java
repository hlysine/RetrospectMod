package theRetrospect.patches.runmods;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import theRetrospect.runmods.InTime;

public class AddRunModsPatch {
    @SpirePatch(
            clz = ModHelper.class,
            method = "initialize"
    )
    public static class InitializeModHelperPatch {
        public static void Postfix() {
            ReflectionHacks.privateMethod(ModHelper.class, "addDifficultyMod", AbstractDailyMod.class).invoke(null, new InTime());
        }
    }

    @SpirePatch(
            clz = CustomModeScreen.class,
            method = "initializeMods"
    )
    public static class InitializeCustomModeScreenPatch {
        public static void Postfix(CustomModeScreen __instance) {
            ReflectionHacks.privateMethod(CustomModeScreen.class, "addDailyMod", String.class, String.class).invoke(__instance, InTime.ID, "r");
        }
    }
}
