package theRetrospect.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.PeacePipe;
import com.megacrit.cardcrawl.relics.Shovel;
import theRetrospect.relics.AnkleWeights;

@SpirePatch(
        clz = Girya.class,
        method = "canSpawn"
)
@SpirePatch(
        clz = PeacePipe.class,
        method = "canSpawn"
)
@SpirePatch(
        clz = Shovel.class,
        method = "canSpawn"
)
public class CampfireRelicsPatch {

    public static boolean canRelicSpawn() {
        int campfireRelicCount = 0;

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof PeacePipe || r instanceof Shovel || r instanceof Girya || r instanceof AnkleWeights) {
                campfireRelicCount++;
            }
        }

        return (campfireRelicCount < 2);
    }

    public static boolean Postfix(boolean __result, AbstractRelic __instance) {
        if (!__result) return false;

        return canRelicSpawn();
    }
}
