package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

@SpirePatch(
        clz = PlayerTurnEffect.class,
        method = "getOrdinalNaming"
)
@SpirePatch(
        clz = BattleStartEffect.class,
        method = "getOrdinalNaming"
)
@SpirePatch(
        clz = TopPanel.class,
        method = "getOrdinalNaming"
)
public class NegativeOrdinalNamingPatch {
    public static SpireReturn<Integer> Prefix(@ByRef int[] i) {
        i[0] = Math.abs(i[0]);
        return SpireReturn.Continue();
    }
}
