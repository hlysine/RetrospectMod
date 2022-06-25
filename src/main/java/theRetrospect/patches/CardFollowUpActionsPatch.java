package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CardFollowUpAction;
import theRetrospect.util.CardUtils;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

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

            List<CardFollowUpAction> followUpActions = CardUtils.getFollowUpActions(___targetCard);
            boolean turnEnding = CardUtils.getTurnEnding(___targetCard);

            // For actions added to the top of the queue, add them in ascending priority
            // so that actions with the highest priority gets added last, achieving first place in queue.
            List<CardFollowUpAction> topItems = followUpActions.stream()
                    .filter(item -> item.onTop)
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
            for (CardFollowUpAction item : topItems) {
                if (turnEnding && item.skipIfEndTurn)
                    continue;
                AbstractDungeon.actionManager.addToTop(item.action);
            }

            // For actions added to the bottom of the queue, add them in descending priority
            // so that actions with the lowest priority gets added last, achieving last place in queue.

            // Reverse the list first, so that items added last get a higher priority implicitly.
            // Relies on the fact that sorted() is stable.
            Deque<CardFollowUpAction> bottomItemsReversed = new ArrayDeque<>();
            for (CardFollowUpAction item : followUpActions) {
                if (!item.onTop) {
                    bottomItemsReversed.addFirst(item);
                }
            }
            List<CardFollowUpAction> bottomItems = bottomItemsReversed.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            for (CardFollowUpAction item : bottomItems) {
                if (turnEnding && item.skipIfEndTurn)
                    continue;
                AbstractDungeon.actionManager.addToBottom(item.action);
            }

            CardUtils.clearFollowUpActions(___targetCard);
            CardUtils.setTurnEnding(___targetCard, false);
        }
    }
}
