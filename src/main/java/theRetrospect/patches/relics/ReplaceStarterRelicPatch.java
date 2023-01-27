package theRetrospect.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import com.megacrit.cardcrawl.vfx.FloatyEffect;
import theRetrospect.relics.AtomicClock;

public class ReplaceStarterRelicPatch {
    @SpirePatch(
            clz = BossRelicSelectScreen.class,
            method = "relicObtainLogic"
    )
    public static class BossRelicSelectPatch {
        public static void Postfix(BossRelicSelectScreen __instance, AbstractRelic r) {
            if (r.relicId.equals(AtomicClock.ID)) {
                r.instantObtain(AbstractDungeon.player, 0, true);
                (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
            }
        }
    }

    @SpirePatch(
            clz = AbstractRelic.class,
            method = "bossObtainLogic"
    )
    public static class BossObtainPatch {
        public static SpireReturn<Void> Prefix(AbstractRelic __instance) {
            if (__instance.relicId.equals(AtomicClock.ID)) {
                __instance.isObtained = true;
                FloatyEffect f_effect = ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "f_effect");
                f_effect.x = 0.0F;
                f_effect.y = 0.0F;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
