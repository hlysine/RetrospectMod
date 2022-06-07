package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

/**
 * Allows cards to be queued.
 * This is a fixed version of {@link com.megacrit.cardcrawl.actions.utility.NewQueueCardAction}.
 */
public class CustomQueueCardAction extends AbstractGameAction {
    private final AbstractCard card;
    private final boolean randomTarget;
    private final AbstractMonster target;
    private final boolean immediateCard;
    private final boolean autoplayCard;

    private CustomQueueCardAction(AbstractCard card, boolean randomTarget, AbstractMonster target, boolean immediateCard, boolean autoplayCard) {
        this.card = card;
        this.randomTarget = randomTarget;
        this.target = target;
        this.immediateCard = immediateCard;
        this.autoplayCard = autoplayCard;
    }

    public CustomQueueCardAction(AbstractCard card, boolean randomTarget, boolean immediateCard, boolean autoplayCard) {
        this(card, randomTarget, null, immediateCard, autoplayCard);
    }

    public CustomQueueCardAction(AbstractCard card, AbstractMonster target, boolean immediateCard, boolean autoplayCard) {
        this(card, false, target, immediateCard, autoplayCard);
    }

    public CustomQueueCardAction() {
        this(null, false, null, false, false);
    }


    public void update() {
        if (this.card == null) {
            if (!queueContainsEndTurnCard()) {
                addCardQueueItem(new CardQueueItem(), false);
            }
        } else if (!queueContains(this.card)) {
            if (this.randomTarget) {
                addCardQueueItem(new CardQueueItem(
                        this.card,
                        true,
                        EnergyPanel.getCurrentEnergy(),
                        false,
                        this.autoplayCard
                ), this.immediateCard);
            } else {
                addCardQueueItem(new CardQueueItem(
                        this.card,
                        this.target,
                        EnergyPanel.getCurrentEnergy(),
                        false,
                        this.autoplayCard
                ), this.immediateCard);
            }
        }
        this.isDone = true;
    }

    private void addCardQueueItem(CardQueueItem item, boolean inFrontOfQueue) {
        if (inFrontOfQueue) {
            AbstractDungeon.actionManager.cardQueue.add(0, item);
        } else {
            AbstractDungeon.actionManager.cardQueue.add(item);
        }
    }

    private boolean queueContains(AbstractCard card) {
        for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card == card) {
                return true;
            }
        }
        return false;
    }

    private boolean queueContainsEndTurnCard() {
        for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card == null) {
                return true;
            }
        }
        return false;
    }
}

