package theRetrospect.subscribers;

import theRetrospect.mechanics.timetravel.TimeManager;

public interface StateChangeSubscriber {
    /**
     * Triggered when the active node of the state tree in {@link TimeManager} changes.
     */
    void onActiveNodeChanged();
}
