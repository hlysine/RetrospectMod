package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import theRetrospect.util.HoverableCardStack;

public class QueueCardIntentAction extends AbstractGameAction {
    private final AbstractCard card;
    private final HoverableCardStack cardStack;

    public QueueCardIntentAction(AbstractCard card) {
        this(card, null);
    }

    public QueueCardIntentAction(AbstractCard card, HoverableCardStack cardStack) {
        this.card = card;
        this.cardStack = cardStack;
    }

    @Override
    public void update() {
        if (cardStack != null)
            cardStack.cards.remove(card);
        AbstractDungeon.effectList.add(new CardFlashVfx(card));
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), true, true), true);
        //AbstractDungeon.actionManager.addToBottom(new NewQueueCardAction(card, true, false, true));
        this.isDone = true;
    }
}
