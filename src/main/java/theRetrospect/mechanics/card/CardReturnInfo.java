package theRetrospect.mechanics.card;

import theRetrospect.minions.AbstractMinionWithCards;

public class CardReturnInfo {
    /**
     * The minion to return the card to. Set it to null to purge the card after use.
     */
    public AbstractMinionWithCards minion = null;

    /**
     * Whether to return the card to the top of the minion's deck, so that it will be played next.
     */
    public boolean toTop = false;
}
