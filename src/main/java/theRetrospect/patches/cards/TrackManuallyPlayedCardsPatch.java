package theRetrospect.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.mechanics.cardAttributes.CardPlaySource;
import theRetrospect.util.CardUtils;

public class TrackManuallyPlayedCardsPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ActionManagerClearPatch {
        public static void Prefix() {
            CardUtils.cardsManuallyPlayedThisTurn.clear();
        }
    }


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class NewRoundPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(GameActionManager __instance) {
            CardUtils.cardsManuallyPlayedThisTurn.clear();
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "totalDiscardedThisTurn");

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class AddPlayedCardPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(GameActionManager __instance) {
            AbstractCard card = __instance.cardQueue.get(0).card;
            if (CardUtils.getPlaySource(card) != CardPlaySource.PLAYER) {
                return SpireReturn.Continue();
            }
            CardUtils.cardsManuallyPlayedThisTurn.add(card);
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisTurn");

                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0]};
            }
        }
    }
}
