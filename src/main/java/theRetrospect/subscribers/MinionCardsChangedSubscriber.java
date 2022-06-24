package theRetrospect.subscribers;

public interface MinionCardsChangedSubscriber {
    /**
     * Triggered when the cards in an {@link theRetrospect.minions.AbstractMinionWithCards} change.
     */
    void onCardsChanged();
}
