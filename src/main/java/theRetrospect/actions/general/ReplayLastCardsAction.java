package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

import java.util.List;
import java.util.function.Predicate;

public class ReplayLastCardsAction extends AbstractGameAction {
    private final Predicate<AbstractCard> filter;
    private final int replayCount;
    private final CardPlaySource source;

    public ReplayLastCardsAction(Predicate<AbstractCard> filter, int replayCount, CardPlaySource source) {
        this.filter = filter;
        this.replayCount = replayCount;
        this.source = source;
    }

    @Override
    public void update() {
        List<AbstractCard> lastPlayedCards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int remaining = replayCount;
        for (int i = lastPlayedCards.size() - 1; i >= 0 && remaining > 0; i--) {
            AbstractCard card = lastPlayedCards.get(i);
            if (filter.test(card)) {
                remaining--;
                AbstractCard copy = CardUtils.makeAdvancedCopy(card, true);
                addToBot(new ShowCardToBePlayedAction(copy));
                addToBot(new QueueCardIntentAction(copy, null, source, true));
            }
        }
        this.isDone = true;
    }
}
