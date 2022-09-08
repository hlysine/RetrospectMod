package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "allowUpgradePreview"
)
public class CurseUpgradePreviewPatch {
    public static boolean Postfix(boolean __result, SingleCardViewPopup __instance, AbstractCard ___card) {
        return __result || (SingleCardViewPopup.enableUpgradeToggle && (___card.canUpgrade() || SingleCardViewPopup.isViewingUpgrade));
    }
}
