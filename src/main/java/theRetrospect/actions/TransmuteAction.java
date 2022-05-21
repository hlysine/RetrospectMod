package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TransmuteAction extends AbstractGameAction {

    private final boolean discardInstead;

    public TransmuteAction(boolean discardInstead) {
        this.discardInstead = discardInstead;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hand.size() <= 0) {
            this.isDone = true;
            return;
        }

        if (discardInstead) {
            if (player.hand.size() == 1) {
                AbstractCard c = player.hand.getTopCard();
                addToBot(new DiscardSpecificCardAction(c));
                addToBot(new GainEnergyFromCardAction(c));
            } else {
                addToBot(new DiscardAction(player, player, 1, false));
                addToBot(new GainEnergyFromCardAction());
            }
        } else {
            if (player.hand.size() == 1) {
                AbstractCard c = player.hand.getTopCard();
                addToBot(new ExhaustSpecificCardAction(c, player.hand));
                addToBot(new GainEnergyFromCardAction(c));
            } else {
                addToBot(new ExhaustAction(player, player, 1, false));
                addToBot(new GainEnergyFromCardAction());
            }
        }
        this.isDone = true;
    }
}
