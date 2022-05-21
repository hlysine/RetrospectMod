package theRetrospect.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.minions.AbstractMinionWithCards;

public interface BeforeMinionPlayCardSubscriber {
    void beforeMinionPlayCard(AbstractMinionWithCards timeline, AbstractCard card);
}
