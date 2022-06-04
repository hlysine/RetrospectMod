package theRetrospect.subscribers;

import theRetrospect.minions.TimelineMinion;

public interface TimelineCollapseSubscriber {
    /**
     * Triggered when a timeline is about to collapse. Can cancel the collapse by returning false.
     *
     * @param timeline     The timeline to be collapsed.
     * @param willCollapse This will be false if another subscriber has already cancelled the collapse.
     * @return Whether the timeline can collapse.
     */
    default boolean beforeTimelineCollapse(TimelineMinion timeline, boolean willCollapse) {
        return true;
    }

    /**
     * Triggered after a timeline has already collapsed.
     *
     * @param timeline The dead timeline minion.
     */
    default void afterTimelineCollapse(TimelineMinion timeline) {

    }
}
