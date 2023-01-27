package theRetrospect.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.subscribers.TimelineConstructSubscriber;

public class CardDynamicVariablePatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "resetAttributes"
    )
    public static class ResetAttributesPatch {
        public static void Prefix(AbstractCard __instance) {
            if (__instance instanceof AbstractBaseCard) {
                AbstractBaseCard card = (AbstractBaseCard) __instance;
                card.timelineCount = card.baseTimelineCount;
                card.isTimelineCountModified = false;
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class ApplyPowersPatch {
        public static void Prefix(AbstractCard __instance) {
            if (__instance instanceof AbstractBaseCard) {
                AbstractBaseCard card = (AbstractBaseCard) __instance;
                card.isTimelineCountModified = false;
                int tmp = card.baseTimelineCount;

                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof TimelineConstructSubscriber) {
                        TimelineConstructSubscriber subscriber = (TimelineConstructSubscriber) p;
                        tmp = subscriber.modifyTimelineCount(card, tmp);
                    }
                }

                if (card.baseTimelineCount != tmp) {
                    card.isTimelineCountModified = true;
                }

                if (tmp < 0) {
                    tmp = 0;
                }

                card.timelineCount = tmp;
            }
        }
    }
}
