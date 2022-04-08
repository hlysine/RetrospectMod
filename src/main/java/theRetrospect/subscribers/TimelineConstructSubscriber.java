package theRetrospect.subscribers;

import theRetrospect.minions.TimelineMinion;

public interface TimelineConstructSubscriber {
    void onTimelineConstruct(TimelineMinion timeline);
}
