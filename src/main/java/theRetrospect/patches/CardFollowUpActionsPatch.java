package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
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
                for (CardAddFieldsPatch.ActionQueueItem item : CardUtils.getFollowUpActions(___targetCard)) {
                    if (item.onTop)
                        AbstractDungeon.actionManager.addToTop(item.action);
                    else
                        AbstractDungeon.actionManager.addToBottom(item.action);
                }
            }
            CardUtils.clearFollowUpActions(___targetCard);
        }
    }
}
