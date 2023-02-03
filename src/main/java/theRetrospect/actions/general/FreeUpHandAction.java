package theRetrospect.actions.general;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.cards.old.curses.Singularity;

import java.util.ArrayList;
import java.util.List;

public class FreeUpHandAction extends AbstractGameAction {
    private final int amount;
    private final AbstractGameAction followUpAction;

    public FreeUpHandAction(int amount, AbstractGameAction followUpAction) {
        this.amount = amount;
        this.followUpAction = followUpAction;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        addToTop(followUpAction);

        int discardAmount = player.hand.size() + amount - BaseMod.MAX_HAND_SIZE;
        if (discardAmount <= 0) {
            this.isDone = true;
            return;
        }

        List<AbstractCard> cardsToDiscard = new ArrayList<>(discardAmount);
        for (int i = player.hand.size() - 1; i >= 0 && cardsToDiscard.size() < discardAmount; i--) {
            AbstractCard card = player.hand.group.get(i);
            if (!(card instanceof Singularity)) {
                cardsToDiscard.add(card);
            }
        }

        for (AbstractCard card : cardsToDiscard) {
            addToTop(new DiscardSpecificCardAction(card));
        }

        this.isDone = true;
    }
}
