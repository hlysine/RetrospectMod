package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CardUtils;

/**
 * Trigger actionAfterUse of a card and clean card fields.
 */
@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class CardFollowUpActionsPatch {
    public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
        if (__instance.isDone) {
            CardUtils.setPlaySource(___targetCard, null);
            CardUtils.setReturnToMinion(___targetCard, null);
            if (!___targetCard.dontTriggerOnUseCard) {
                for (AbstractGameAction action : CardUtils.getFollowUpActions(___targetCard)) {
                    AbstractDungeon.actionManager.addToBottom(action);
                }
            }
            CardUtils.clearFollowUpActions(___targetCard);
        }
    }
}
