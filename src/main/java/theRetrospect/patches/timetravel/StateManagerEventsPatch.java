package theRetrospect.patches.timetravel;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.mechanics.timetravel.TimeManager;

public class StateManagerEventsPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class StartOfTurnPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert() {
            TimeManager.atStartOfTurn();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.NewExprMatcher(EnableEndTurnButtonAction.class);

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ActionManagerClearPatch {
        public static void Prefix() {
            TimeManager.reset();
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "callEndOfTurnActions"
    )
    public static class EndOfTurnPatch {
        public static void Prefix() {
            TimeManager.atEndOfTurn();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class OnVictoryPatch {
        public static void Postfix() {
            TimeManager.atEndOfTurn();
        }
    }
}
