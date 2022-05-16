package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.util.MinionUtils;

/**
 * These patches forward some player triggers to minions
 */
public class TimelineEventsPatch {
    /**
     * Forward applyPowers trigger to minions so that timeline cards can update descriptions according to player powers.
     */
    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class CardGroupApplyPowerPatch {
        public static void Postfix(CardGroup __instance) {
            if (__instance.type == CardGroup.CardGroupType.HAND && AbstractDungeon.player != null)
                MinionUtils.getMinions(AbstractDungeon.player).monsters.forEach(AbstractMonster::applyPowers);
        }
    }

    /**
     * Forward onAfterUseCard trigger to minions
     */
    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class OnAfterUseCardPatch {
        @SpireInsertPatch(
                rloc = 7
        )
        public static void Insert(UseCardAction __instance, AbstractCard ___targetCard, float ___duration) {
            RetrospectMod.logger.info("Start insert patch");
            for (AbstractMonster monster : MinionUtils.getMinions(AbstractDungeon.player).monsters) {
                for (AbstractPower power : monster.powers) {
                    if (!___targetCard.dontTriggerOnUseCard) {
                        power.onAfterUseCard(___targetCard, __instance);
                        RetrospectMod.logger.info("Trigger on after use card");
                    }
                }
            }
            RetrospectMod.logger.info("End insert patch");
        }
    }
}