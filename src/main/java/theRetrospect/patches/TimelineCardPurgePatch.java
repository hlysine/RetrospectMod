package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.actions.general.ShowTimelineCardAndPurgeAction;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardReturnInfo;
import theRetrospect.util.CardUtils;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class TimelineCardPurgePatch {

    private static boolean shouldBePurged(AbstractCard card) {
        CardReturnInfo info = CardUtils.getReturnInfo(card);
        if (info.minion == null) return true;
        if (info.minion.isDeadOrEscaped()) return true;
        return false;
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(UseCardAction __instance, AbstractCard ___targetCard) {
        if (___targetCard.purgeOnUse
                && CardUtils.getPlaySource(___targetCard) == CardPlaySource.TIMELINE
                && shouldBePurged(___targetCard)) {
            AbstractDungeon.actionManager.addToTop(new ShowTimelineCardAndPurgeAction(___targetCard));
            __instance.isDone = true;
            AbstractDungeon.player.cardInUse = null;

            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");

            return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}