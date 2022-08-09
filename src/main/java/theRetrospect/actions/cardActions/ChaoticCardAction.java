package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.general.CustomQueueCardAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

public class ChaoticCardAction extends AbstractGameAction {
    private final AbstractBaseCard avert;
    private final AbstractCard.CardType cardType;

    public ChaoticCardAction(AbstractBaseCard avert, AbstractCard.CardType cardType) {
        this.avert = avert;
        this.cardType = cardType;
    }

    @Override
    public void update() {
        boolean cardPlayed = false;
        if (AbstractDungeon.player.discardPile.size() > 0) {
            AbstractCard card = AbstractDungeon.player.discardPile.getRandomCard(cardType, true);
            if (card != null) {
                AbstractDungeon.player.discardPile.removeCard(card);
                CardUtils.addFollowUpActionToTop(card, new ConstructMultipleTimelineAction(avert, avert.timelineCount), false, 100);
                addToBot(new ShowCardToBePlayedAction(card, avert.current_x, avert.current_y));
                addToBot(new CustomQueueCardAction(card, true, true, true));
                cardPlayed = true;
            }
        }
        if (!cardPlayed) {
            addToBot(new ConstructMultipleTimelineAction(avert, avert.timelineCount));
        }
        this.isDone = true;
    }
}
