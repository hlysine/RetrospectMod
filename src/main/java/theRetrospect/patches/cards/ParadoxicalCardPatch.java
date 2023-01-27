package theRetrospect.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.mechanics.cardAttributes.CardPlaySource;
import theRetrospect.util.CardUtils;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "useCard"
)
public class ParadoxicalCardPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractPlayer __instance, AbstractCard ___c) {
        if (___c instanceof AbstractBaseCard) {
            AbstractBaseCard card = (AbstractBaseCard) ___c;
            if (card.paradoxical) {
                if (CardUtils.getPlaySource(card) == CardPlaySource.CARD) {
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Paradox()));
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");

            return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}