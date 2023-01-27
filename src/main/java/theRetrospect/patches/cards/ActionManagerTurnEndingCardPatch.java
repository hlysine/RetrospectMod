package theRetrospect.patches.cards;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CardUtils;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndTurnEarlySequence"
)
public class ActionManagerTurnEndingCardPatch {
    public static void Prefix() {
        for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
            if (item.autoplayCard) {
                CardUtils.setTurnEnding(item.card, true);
            }
        }

        if (AbstractDungeon.player.cardInUse != null)
            CardUtils.setTurnEnding(AbstractDungeon.player.cardInUse, true);

        AbstractGameAction currentAction = AbstractDungeon.actionManager.currentAction;
        if (currentAction != null) {
            if (currentAction instanceof UseCardAction && !currentAction.isDone) {
                CardUtils.setTurnEnding(
                        ReflectionHacks.getPrivate(currentAction, UseCardAction.class, "targetCard"),
                        true
                );
            }
        }

        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof UseCardAction) {
                CardUtils.setTurnEnding(
                        ReflectionHacks.getPrivate(action, UseCardAction.class, "targetCard"),
                        true
                );
            }
        }
    }
}
