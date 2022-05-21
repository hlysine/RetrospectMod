package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import theRetrospect.characters.TheRetrospect;
import theRetrospect.util.MinionUtils;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "preBattlePrep"
)
public class PlayerMaxMinionsPatch {
    public static void Postfix(AbstractPlayer __instance) {
        MinionUtils.changeMaxMinionAmount(__instance, TheRetrospect.MAX_MINIONS);
    }
}
