package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.general.GainEnergyFromCardAction;

public class TransmuteAction extends AbstractGameAction {

    private final int amount;
    private final boolean discardInstead;

    public TransmuteAction(int amount, boolean discardInstead) {
        this.amount = amount;
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
            if (player.hand.size() <= this.amount) {
                for (AbstractCard card : player.hand.group) {
                    addToBot(new DiscardSpecificCardAction(card));
                    addToBot(new GainEnergyFromCardAction(card));
                }
            } else {
                addToBot(new DiscardAction(player, player, amount, false));
                addToBot(new GainEnergyFromCardAction());
            }
        } else {
            if (player.hand.size() <= this.amount) {
                for (AbstractCard card : player.hand.group) {
                    addToBot(new ExhaustSpecificCardAction(card, player.hand));
                    addToBot(new GainEnergyFromCardAction(card));
                }
            } else {
                addToBot(new ExhaustAction(player, player, amount, false));
                addToBot(new GainEnergyFromCardAction());
            }
        }
        this.isDone = true;
    }
}
