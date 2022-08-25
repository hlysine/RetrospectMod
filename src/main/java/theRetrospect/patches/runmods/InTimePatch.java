package theRetrospect.patches.runmods;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.runmods.InTime;

public class InTimePatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "update"
    )
    public static class UpdateAbstractDungeonPatch {
        public static void Postfix() {
            if (ModHelper.isModEnabled(InTime.ID)) {
                InTime.update();
            }
        }
    }

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
            if (ModHelper.isModEnabled(InTime.ID)) {
                InTime.atStartOfTurn();
            }
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
            method = "callEndOfTurnActions"
    )
    public static class EndOfTurnPatch {
        public static void Prefix() {
            if (ModHelper.isModEnabled(InTime.ID)) {
                InTime.atEndOfTurn();
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class OnVictoryPatch {
        public static void Postfix() {
            if (ModHelper.isModEnabled(InTime.ID)) {
                InTime.atEndOfTurn();
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class VampireDamagePatch {
        public static void Postfix(AbstractMonster __instance, DamageInfo info) {
            if (ModHelper.isModEnabled(InTime.ID)) {
                if (!(__instance instanceof AbstractFriendlyMonster)) {
                    if (info.owner == AbstractDungeon.player || info.owner instanceof AbstractFriendlyMonster) {
                        InTime.onAttack(__instance.lastDamageTaken);
                    }
                }
            }
        }
    }

    private static final Color hbTimeColor = new Color(0.7F, 0.9F, 0.5F, 1.0F);
    private static final Color hbTimeColorActive = new Color(0.8F, 0.4F, 1F, 1.0F);

    public static String getTimeFromHealth(int hp) {
        hp = (int) Math.floor(hp * InTime.HP_LOSS_INTERVAL);
        int minutes = (int) Math.floor(hp / 60f);
        int seconds = hp % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderHealthText"
    )
    public static class HealthTimeTextPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCreature __instance, SpriteBatch sb, float y) {
            if (__instance instanceof AbstractPlayer) return; // player is handled in another patch
            if (ModHelper.isModEnabled(InTime.ID)) {
                if (__instance.currentHealth > 0) {
                    FontHelper.renderFontCentered(
                            sb,
                            FontHelper.healthInfoFont,
                            getTimeFromHealth(__instance.currentHealth),
                            __instance.hb.cX,
                            y + 6f * Settings.scale,
                            hbTimeColor
                    );
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class PlayerHealthTimeTextPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, SpriteBatch sb) {
            if (ModHelper.isModEnabled(InTime.ID)) {
                if (__instance.currentHealth > 0) {
                    FontHelper.renderFontCentered(
                            sb,
                            FontHelper.healthInfoFont,
                            getTimeFromHealth(__instance.currentHealth),
                            __instance.hb.cX,
                            __instance.hb.cY - __instance.hb.height / 2.0F + 6f * Settings.scale,
                            InTime.isRunning() ? hbTimeColorActive : hbTimeColor
                    );
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "healthHb");

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
