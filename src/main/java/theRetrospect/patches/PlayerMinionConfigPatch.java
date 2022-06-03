package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.characters.TheRetrospect;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = SpirePatch.CONSTRUCTOR
)
public class PlayerMinionConfigPatch {
    public static void Postfix(AbstractPlayer __instance) {
        MinionUtils.setBaseMinionCount(__instance, TheRetrospect.MAX_MINIONS);
        MinionUtils.setBaseMinionPowerChance(__instance, 0);
        MinionUtils.setBaseMinionAttackTargetChance(__instance, 0);
    }
}
