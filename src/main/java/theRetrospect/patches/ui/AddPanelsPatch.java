package theRetrospect.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.ui.UIManager;

public class AddPanelsPatch {

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "update"
    )
    public static class OverlayMenuUpdatePatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(OverlayMenu __instance) {
            UIManager.turnCardsPanel.updatePositions();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "updatePositions");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "render"
    )
    public static class OverlayMenuRenderPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(OverlayMenu __instance, SpriteBatch __sb) {
            UIManager.turnCardsPanel.render(__sb);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "showCombatPanels"
    )
    public static class OverlayMenuShowCombatPanelsPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(OverlayMenu __instance) {
            UIManager.turnCardsPanel.show();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "show");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "hideCombatPanels"
    )
    public static class OverlayMenuHideCombatPanelsPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(OverlayMenu __instance) {
            UIManager.turnCardsPanel.hide();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "hide");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "render"
    )
    public static class CardRewardScreenRenderPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardRewardScreen __instance, SpriteBatch __sb) {
            UIManager.turnCardsPanel.render(__sb);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "render"
    )
    public static class HandCardSelectScreenRenderPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(HandCardSelectScreen __instance, SpriteBatch __sb) {
            UIManager.turnCardsPanel.render(__sb);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
