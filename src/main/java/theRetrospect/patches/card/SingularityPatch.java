package theRetrospect.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.general.FreeUpHandAction;
import theRetrospect.actions.general.SpecificCardToHandAction;
import theRetrospect.cards.old.curses.Singularity;

@SpirePatch(
        clz = CardGroup.class,
        method = "refreshHandLayout"
)
public class SingularityPatch {
    public static void Prefix() {
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof Singularity) {
                AbstractDungeon.actionManager.addToTop(new FreeUpHandAction(1, new SpecificCardToHandAction(card, AbstractDungeon.player.discardPile)));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof Singularity) {
                AbstractDungeon.actionManager.addToTop(new FreeUpHandAction(1, new SpecificCardToHandAction(card, AbstractDungeon.player.drawPile)));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof Singularity) {
                AbstractDungeon.actionManager.addToTop(new FreeUpHandAction(1, new SpecificCardToHandAction(card, AbstractDungeon.player.exhaustPile)));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.limbo.group) {
            if (card instanceof Singularity) {
                AbstractDungeon.actionManager.addToTop(new FreeUpHandAction(1, new SpecificCardToHandAction(card, AbstractDungeon.player.limbo)));
            }
        }
    }
}
