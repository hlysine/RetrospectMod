package theRetrospect.mechanics.card;

/**
 * Denotes the source that causes a card to be played.
 */
public enum CardPlaySource {
    /**
     * The card is played by the player manually.
     */
    PLAYER,
    /**
     * The card is played by a timeline.
     */
    TIMELINE,
    /**
     * The card is played by another card.
     */
    CARD,
    /**
     * The card is played by a relic effect.
     */
    RELIC
}
