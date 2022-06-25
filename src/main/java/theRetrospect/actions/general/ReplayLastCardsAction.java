package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ReplayLastCardsAction extends AbstractGameAction {
    private final AbstractCard originatingCard;
    private final Predicate<AbstractCard> filter;
    private final int replayCount;
    private final CardPlaySource source;

    public ReplayLastCardsAction(AbstractCard originatingCard, Predicate<AbstractCard> filter, int replayCount, CardPlaySource source) {
        this.originatingCard = originatingCard;
        this.filter = filter;
        this.replayCount = replayCount;
        this.source = source;
    }

    @Override
    public void update() {
        float origin_x = originatingCard.current_x;
        float origin_y = originatingCard.current_y;

        List<AbstractCard> lastPlayedCards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int remaining = replayCount;
        List<AbstractCard> replayCards = new ArrayList<>(replayCount);
        for (int i = lastPlayedCards.size() - 1; i >= 0 && remaining > 0; i--) {
            AbstractCard card = lastPlayedCards.get(i);
            if (card != originatingCard && (filter == null || filter.test(card))) {
                remaining--;
                AbstractCard copy = CardUtils.makeAdvancedCopy(card, true);
                replayCards.add(0, copy);
            }
        }
        addToBot(new ShowCardToBePlayedAction(replayCards, origin_x, origin_y));
        CallbackUtils.ForEachLoop(replayCards, (card, next) -> {
            CardUtils.addFollowUpActionToBottom(card, new RunnableAction(next), true, 0);
            addToBot(new QueueCardIntentAction(card, null, source, true));
        });
        this.isDone = true;
    }
}
