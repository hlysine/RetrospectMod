package theRetrospect.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.util.CardUtils;

/**
 * Trigger follow-up actions of a card and clean card fields.
 */
public class CardFollowUpActionsPatch {
    private static void triggerFollowUpActions(AbstractCard card) {
        CardUtils.setPlaySource(card, null);
        CardUtils.setReturnInfo(card, null, false);

        CardUtils.createFollowUpActionHandler(card).scheduleFollowUpActions();
    }

    /**
     * Trigger follow-up actions after a card is successfully played.
     */
    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionPatch {
        public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
            if (__instance.isDone) {
                triggerFollowUpActions(___targetCard);
            }
        }
    }

    /**
     * Trigger follow-up actions after a card is auto-played but has no targets to act on.
     * For some reason UseCardAction is not enqueued for this specific case.
     */
    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class ActionManagerGetNextActionPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            CardQueueItem item = __instance.cardQueue.get(0);
            if (item.card != null && item.autoplayCard) {
                triggerFollowUpActions(item.card);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "isDeadOrEscaped");

                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1};
            }
        }
    }
}
