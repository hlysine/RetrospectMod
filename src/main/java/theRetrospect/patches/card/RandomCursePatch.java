package theRetrospect.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.cards.old.curses.Singularity;

import java.util.ArrayList;

public class RandomCursePatch {
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {}
    )
    public static class GetCursePatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void Insert(ArrayList<String> tmp) {
            tmp.remove(Singularity.ID);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Random.class, "random");

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {AbstractCard.class, Random.class}
    )
    public static class GetCurseWithRandomPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void Insert(ArrayList<String> tmp) {
            tmp.remove(Singularity.ID);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Random.class, "random");

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
