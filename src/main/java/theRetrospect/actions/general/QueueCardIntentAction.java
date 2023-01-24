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
    private final boolean autoplayCard;

    /**
     * Queues a card to be played. The card will be purged after being played.
     *
     * @param card           The card to queue.
     * @param source         The source of the card.
     * @param inFrontOfQueue Whether to add the card to the front of the card queue.
     * @param autoplayCard   Whether to spend energy when playing the card. The card will fail to play if there is not enough energy and autoplay is off.
     */
    public QueueCardIntentAction(AbstractCard card, CardPlaySource source, boolean inFrontOfQueue, boolean autoplayCard) {
        this(card, null, source, inFrontOfQueue, autoplayCard);
    }

    /**
     * Queues a card to be played. The card will be purged after being played.
     *
     * @param card           The card to queue.
     * @param cardStack      The card stack to remove the card from.
     * @param source         The source of the card.
     * @param inFrontOfQueue Whether to add the card to the front of the card queue.
     * @param autoplayCard   Whether to spend energy when playing the card. The card will fail to play if there is not enough energy and autoplay is off.
     */
    public QueueCardIntentAction(AbstractCard card, HoverableCardStack cardStack, CardPlaySource source, boolean inFrontOfQueue, boolean autoplayCard) {
        this.card = card;
        this.cardStack = cardStack;
        this.source = source;
        this.inFrontOfQueue = inFrontOfQueue;
        this.autoplayCard = autoplayCard;
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
        addToTop(new CustomQueueCardAction(card, true, inFrontOfQueue, autoplayCard));
        this.isDone = true;
    }
}
