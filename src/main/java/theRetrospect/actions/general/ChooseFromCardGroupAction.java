package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChooseFromCardGroupAction extends AbstractGameAction {
    private final CardGroup cardGroup;
    private final String menuText;
    private final Consumer<AbstractCard> selectedCardAction;
    private final int numberOfCards;
    private final boolean optional;
    private final boolean forUpgrade;

    public ChooseFromCardGroupAction(List<AbstractCard> cards, String menuText, int numberOfCards, boolean optional, boolean forUpgrade, Consumer<AbstractCard> selectedCardAction) {
        this.cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        this.cardGroup.group.addAll(cards);
        this.menuText = menuText;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.forUpgrade = forUpgrade;
        this.selectedCardAction = selectedCardAction;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.cardGroup.isEmpty() || this.numberOfCards <= 0) {
                this.isDone = true;
                return;
            }
            if (this.cardGroup.size() <= this.numberOfCards && !this.optional) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>(this.cardGroup.group);
                for (AbstractCard c : cardsToMove) {
                    selectedCardAction.accept(c);
                }
                this.isDone = true;
                return;
            }
            if (this.optional) {
                AbstractDungeon.gridSelectScreen.open(this.cardGroup, this.numberOfCards, true, menuText);
            } else {
                AbstractDungeon.gridSelectScreen.open(this.cardGroup, this.numberOfCards, menuText, forUpgrade);
            }
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                selectedCardAction.accept(c);
            }
            for (AbstractCard c : this.cardGroup.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}