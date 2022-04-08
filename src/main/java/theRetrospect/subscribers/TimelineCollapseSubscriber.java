package theRetrospect.subscribers;

import theRetrospect.minions.TimelineMinion;

public interface TimelineCollapseSubscriber {
    void onTimelineCollapse(TimelineMinion timeline);
}
