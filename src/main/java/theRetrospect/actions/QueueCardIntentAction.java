package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import theRetrospect.util.CardUtils;
import theRetrospect.util.HoverableCardStack;

public class QueueCardIntentAction extends AbstractGameAction {
    private final AbstractCard card;
    private final HoverableCardStack cardStack;
    private final boolean countAsReplay;

    public QueueCardIntentAction(AbstractCard card, boolean countAsReplay) {
        this(card, null, countAsReplay);
    }

    public QueueCardIntentAction(AbstractCard card, HoverableCardStack cardStack, boolean countAsReplay) {
        this.card = card;
        this.cardStack = cardStack;
        this.countAsReplay = countAsReplay;
    }

    @Override
    public void update() {
        if (cardStack != null)
            cardStack.cards.remove(card);
        card.targetDrawScale = 1f;
        card.purgeOnUse = true;
        if (countAsReplay)
            CardUtils.setIsBeingReplayed(card, true);
        AbstractDungeon.player.limbo.addToBottom(card);
        AbstractDungeon.effectList.add(new CardFlashVfx(card));
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), true, true));
        this.isDone = true;
    }
}
