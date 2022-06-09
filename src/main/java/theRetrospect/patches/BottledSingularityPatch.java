package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.util.CardUtils;

import java.util.ArrayList;


public class BottledSingularityPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            CardUtils.setIsInBottledSingularity(result, CardUtils.getIsInBottledSingularity(self));
            return result;
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "getGroupWithoutBottledCards"
    )
    public static class GetGroupWithoutBottledCardsPatch {
        public static CardGroup Postfix(CardGroup ret, CardGroup source) {
            ArrayList<AbstractCard> group = ret.group;
            for (int i = group.size() - 1; i >= 0; i--) {
                AbstractCard card = group.get(i);
                if (CardUtils.getIsInBottledSingularity(card)) {
                    ret.removeCard(card);
                }
            }
            return ret;
        }
    }


    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class InitializeDeckPatch {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"copy", "placeOnTop"}
        )
        public static void Insert(CardGroup __instance, CardGroup copy, ArrayList<AbstractCard> placeOnTop) {
            for (AbstractCard card : copy.group) {
                if (CardUtils.getIsInBottledSingularity(card)) {
                    placeOnTop.add(card);
                    __instance.removeCard(card);
                    __instance.addToTop(card);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "masterHandSize");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}