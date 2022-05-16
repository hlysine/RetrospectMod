package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.util.CardUtils;

/**
 * Trigger actionAfterUse of a card
 */
@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class CardActionAfterUsePatch {
    public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
        if (__instance.isDone) {
            if (!___targetCard.dontTriggerOnUseCard) {
                Runnable action = CardUtils.getActionAfterUse(___targetCard);
                if (action != null) action.run();
            }
            CardUtils.setActionAfterUse(___targetCard, null);
        }
    }
}
