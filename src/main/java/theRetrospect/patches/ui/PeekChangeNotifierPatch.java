package theRetrospect.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.mechanics.timetravel.TimeManager;

// todo: also handle exiting peek via other methods
public class PeekChangeNotifierPatch {
    @SpirePatch(
            clz = PeekButton.class,
            method = "render"
    )
    public static class StartPeekingPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(PeekButton __instance) {
            TimeManager.onPeekStatusChanged();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "hideBlackScreen");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = PeekButton.class,
            method = "render"
    )
    public static class StopPeekingPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(PeekButton __instance) {
            TimeManager.onPeekStatusChanged();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "showBlackScreen");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "closeCurrentScreen"
    )
    public static class ScreenClosePatch {
        public static void Prefix() {
            if (PeekButton.isPeeking) {
                PeekButton.isPeeking = false;
                TimeManager.onPeekStatusChanged();
            }
            TimeManager.peekMinion = null;
        }
    }
}
