package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ChooseFromDiscardPileAction extends AbstractGameAction {
    private final String menuText;
    private final Consumer<AbstractCard> selectedCardAction;
    private final AbstractPlayer player = AbstractDungeon.player;
    private final int numberOfCards;
    private final boolean optional;

    public ChooseFromDiscardPileAction(String menuText, int numberOfCards, boolean optional, Consumer<AbstractCard> selectedCardAction) {
        this.menuText = menuText;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.selectedCardAction = selectedCardAction;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.discardPile.isEmpty() || this.numberOfCards <= 0) {
                this.isDone = true;
                return;
            }
            if (this.player.discardPile.size() <= this.numberOfCards && !this.optional) {
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>(this.player.discardPile.group);
                for (AbstractCard c : cardsToMove) {
                    selectedCardAction.accept(c);
                }
                this.isDone = true;
                return;
            }
            if (this.optional) {
                AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, menuText);
            } else {
                AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, menuText, false);
            }
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                selectedCardAction.accept(c);
            }
            for (AbstractCard c : this.player.discardPile.group) {
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