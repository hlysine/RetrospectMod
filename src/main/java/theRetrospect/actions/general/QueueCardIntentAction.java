package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;
import theRetrospect.util.HoverableCardStack;

public class QueueCardIntentAction extends AbstractGameAction {
    private final AbstractCard card;
    private final HoverableCardStack cardStack;
    private final CardPlaySource source;
    private final boolean inFrontOfQueue;

    public QueueCardIntentAction(AbstractCard card, CardPlaySource source, boolean inFrontOfQueue) {
        this(card, null, source, inFrontOfQueue);
    }

    public QueueCardIntentAction(AbstractCard card, HoverableCardStack cardStack, CardPlaySource source, boolean inFrontOfQueue) {
        this.card = card;
        this.cardStack = cardStack;
        this.source = source;
        this.inFrontOfQueue = inFrontOfQueue;
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
        addToTop(new CustomQueueCardAction(card, true, inFrontOfQueue, true));
        this.isDone = true;
    }
}
