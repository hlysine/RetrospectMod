package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class GainEnergyFromCardAction extends AbstractGameAction {

    private final AbstractCard card;

    public GainEnergyFromCardAction(AbstractCard card) {
        this.card = card;
    }

    public GainEnergyFromCardAction() {
        this(null);
    }

    @Override
    public void update() {
        AbstractCard target = getCard();
        if (target == null) {
            this.isDone = true;
            return;
        }

        int cost = target.cost;
        if (cost > 0)
            addToBot(new GainEnergyAction(cost));
        else if (cost == -1)
            addToBot(new GainEnergyAction(EnergyPanel.getCurrentEnergy()));
        this.isDone = true;
    }

    private AbstractCard getCard() {
        if (card != null) return card;

        if (AbstractDungeon.handCardSelectScreen.selectedCards.size() <= 0) {
            return null;
        } else {
            return AbstractDungeon.handCardSelectScreen.selectedCards.getTopCard();
        }
    }
}
