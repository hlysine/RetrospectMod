package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;
import theRetrospect.util.HoverableCardStack;

public class QueueCardIntentAction extends AbstractGameAction {
    private final AbstractCard card;
    private final HoverableCardStack cardStack;
    private final CardPlaySource source;

    public QueueCardIntentAction(AbstractCard card, CardPlaySource source) {
        this(card, null, source);
    }

    public QueueCardIntentAction(AbstractCard card, HoverableCardStack cardStack, CardPlaySource source) {
        this.card = card;
        this.cardStack = cardStack;
        this.source = source;
    }

    @Override
    public void update() {
        if (cardStack != null)
            cardStack.cards.remove(card);
        card.targetDrawScale = 1f;
        card.purgeOnUse = true;
        CardUtils.setPlaySource(card, source);
        AbstractDungeon.player.limbo.addToBottom(card);
        AbstractDungeon.effectList.add(new CardFlashVfx(card));
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), true, true));
        this.isDone = true;
    }
}
