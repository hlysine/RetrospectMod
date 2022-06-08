package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

public class GainEnergyFromCardAction extends AbstractGameAction {

    private final List<AbstractCard> cards;

    public GainEnergyFromCardAction(AbstractCard card) {
        this.cards = new ArrayList<>(1);
        this.cards.add(card);
    }

    public GainEnergyFromCardAction(List<AbstractCard> cards) {
        this.cards = cards;
    }

    public GainEnergyFromCardAction() {
        this.cards = null;
    }

    @Override
    public void update() {
        List<AbstractCard> target = getCards();
        if (target == null) {
            this.isDone = true;
            return;
        }

        int totalGain = 0;
        for (AbstractCard card : target) {
            int cost = card.cost;
            if (cost > 0)
                totalGain += cost;
            else if (cost == -1)
                totalGain += EnergyPanel.getCurrentEnergy();
        }

        addToBot(new GainEnergyAction(totalGain));

        this.isDone = true;
    }

    private List<AbstractCard> getCards() {
        if (cards != null) return cards;

        if (AbstractDungeon.handCardSelectScreen.selectedCards.size() <= 0) {
            return null;
        } else {
            return AbstractDungeon.handCardSelectScreen.selectedCards.group;
        }
    }
}
