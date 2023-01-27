package theRetrospect.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.mechanics.card.CardReturnInfo;
import theRetrospect.util.CardUtils;

public class CardReturnToMinionPatch {

    @SpirePatch(
            clz = Soul.class,
            method = SpirePatch.CLASS
    )
    public static class SoulAddFieldPatch {
        public static final SpireField<AbstractMinionWithCards> returnToMinion = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz = Soul.class,
            method = "setSharedVariables"
    )
    public static class SoulResetReturnToMinionPatch {
        public static void Postfix(Soul __instance) {
            SoulAddFieldPatch.returnToMinion.set(__instance, null);
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(UseCardAction __instance, AbstractCard ___targetCard) {
            CardReturnInfo info = CardUtils.getReturnInfo(___targetCard);
            if (info.minion != null) {
                if (!info.minion.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.removeFromQueue(___targetCard);
                    if (AbstractDungeon.player.limbo.contains(___targetCard)) {
                        AbstractDungeon.player.limbo.removeCard(___targetCard);
                    }
                    ___targetCard.unhover();
                    ___targetCard.untip();
                    ___targetCard.stopGlowing();

                    ___targetCard.exhaustOnUseOnce = false;
                    ___targetCard.dontTriggerOnUseCard = false;

                    ___targetCard.shrink();
                    ___targetCard.darken(false);
                    CardUtils.soulReturnToMinion(AbstractDungeon.getCurrRoom().souls, ___targetCard, info.minion, info.toTop);

                    __instance.isDone = true;
                    AbstractDungeon.player.cardInUse = null;

                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());

                    return SpireReturn.Return();
                }
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

    @SpirePatch(
            clz = Soul.class,
            method = "update"
    )
    public static class SoulFinishPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(Soul __instance) {
            AbstractCard card = __instance.card;
            AbstractMinionWithCards minion = SoulAddFieldPatch.returnToMinion.get(__instance);
            if (minion != null) {
                card.targetDrawScale = 0.75F;
                card.setAngle(0.0F);
                card.lighten(false);
                card.clearPowers();
                card.current_x = card.target_x = minion.drawX;
                card.current_y = card.target_y = minion.drawY;
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                    AbstractDungeon.player.hand.applyPowers();
                }
                __instance.isReadyForReuse = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.NewExprMatcher(EmpowerEffect.class);

                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
