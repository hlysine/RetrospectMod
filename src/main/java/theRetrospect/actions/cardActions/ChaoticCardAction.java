package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.general.CustomQueueCardAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ChaoticCardAction extends AbstractGameAction {
    private final AbstractBaseCard chaoticCard;
    private final CardGroup cardGroup;
    private final AbstractCard.CardType cardType;

    public ChaoticCardAction(AbstractBaseCard chaoticCard, CardGroup cardGroup, AbstractCard.CardType cardType) {
        this.chaoticCard = chaoticCard;
        this.cardGroup = cardGroup;
        this.cardType = cardType;
    }

    @Override
    public void update() {
        List<AbstractCard> candidates = cardGroup.group.stream()
                .filter(c -> c != chaoticCard && c.type == cardType)
                .sorted()
                .collect(Collectors.toList());
        if (!candidates.isEmpty()) {
            AbstractCard card = candidates.get(AbstractDungeon.cardRng.random(candidates.size() - 1));
            cardGroup.removeCard(card);
            CardUtils.addFollowUpActionToTop(card, new ConstructMultipleTimelineAction(chaoticCard, chaoticCard.timelineCount), false, 100);
            addToBot(new ShowCardToBePlayedAction(card, chaoticCard.current_x, chaoticCard.current_y));
            addToBot(new CustomQueueCardAction(card, true, true, true));
        } else {
            addToBot(new ConstructMultipleTimelineAction(chaoticCard, chaoticCard.timelineCount));
        }
        this.isDone = true;
    }
}
