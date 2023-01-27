package theRetrospect.subscribers;

public interface EnergySourceChangedSubscriber {
    /**
     * Triggered when the player's energy source is changed via {@link theRetrospect.mechanics.timeline.EnergySwitch}.
     */
    void onEnergySourceChanged();
}
