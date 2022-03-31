package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.util.CardUtils;

import java.util.List;
import java.util.function.Predicate;

public class ReplayLastCardsAction extends AbstractGameAction {
    private final Predicate<AbstractCard> filter;
    private final int replayCount;
    private final boolean allowNonReplayable;
    private final boolean countAsReplay;

    public ReplayLastCardsAction(Predicate<AbstractCard> filter, int replayCount, boolean allowNonReplayable, boolean countAsReplay) {
        this.filter = filter;
        this.replayCount = replayCount;
        this.allowNonReplayable = allowNonReplayable;
        this.countAsReplay = countAsReplay;
    }

    @Override
    public void update() {
        List<AbstractCard> lastPlayedCards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int remaining = replayCount;
        for (int i = lastPlayedCards.size() - 2; i >= 0 && remaining > 0; i--) {
            AbstractCard card = lastPlayedCards.get(i);
            if (filter.test(card) && (allowNonReplayable || CardUtils.isCardReplayable(card))) {
                remaining--;
                addToBot(new QueueCardIntentAction(CardUtils.makeStatEquivalentCopyWithPosition(card),null, countAsReplay));
            }
        }
        this.isDone = true;
    }
}
