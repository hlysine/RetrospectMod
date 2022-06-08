package theRetrospect.patches.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.ui.UIManager;

public class AddScreensPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "update"
    )
    public static class AbstractDungeonUpdatePatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, AbstractDungeon.CurrentScreen ___screen) {
            if (___screen == UIManager.TURN_CARDS_VIEW) {
                UIManager.turnCardsViewScreen.update();
            } else if (___screen == UIManager.TIMELINE_CARDS_VIEW) {
                UIManager.minionCardsViewScreen.update();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "render"
    )
    public static class AbstractDungeonRenderPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SpriteBatch __sb, AbstractDungeon.CurrentScreen ___screen) {
            if (___screen == UIManager.TURN_CARDS_VIEW) {
                UIManager.turnCardsViewScreen.render(__sb);
            } else if (___screen == UIManager.TIMELINE_CARDS_VIEW) {
                UIManager.minionCardsViewScreen.render(__sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "closeCurrentScreen"
    )
    public static class AbstractDungeonCloseCurrentScreenPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon.CurrentScreen ___screen) {
            if (___screen == UIManager.TURN_CARDS_VIEW || ___screen == UIManager.TIMELINE_CARDS_VIEW) {
                AbstractDungeon.overlayMenu.cancelButton.hide();
                ReflectionHacks.privateStaticMethod(AbstractDungeon.class, "genericScreenOverlayReset").invoke();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "screen");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "openPreviousScreen"
    )
    public static class AbstractDungeonOpenPreviousScreenPatch {

        public static SpireReturn<Void> Prefix(AbstractDungeon.CurrentScreen __s) {
            if (__s == UIManager.TURN_CARDS_VIEW) {
                UIManager.turnCardsViewScreen.reopen();
                return SpireReturn.Return();
            } else if (__s == UIManager.TIMELINE_CARDS_VIEW) {
                UIManager.minionCardsViewScreen.reopen();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
