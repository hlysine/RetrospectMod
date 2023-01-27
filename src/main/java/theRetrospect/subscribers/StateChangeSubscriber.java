package theRetrospect.subscribers;

public interface StateChangeSubscriber {
    /**
     * Triggered when the active node of the state tree in {@link theRetrospect.mechanics.timetravel.StateManager} changes.
     */
    void onActiveNodeChanged();
}
