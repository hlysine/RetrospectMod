package theRetrospect.subscribers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.minions.TimelineMinion;

public interface TimelineConstructSubscriber {
    /**
     * Modify the number of timelines constructed by a timeline construction card.
     *
     * @param constructionCard  The card that constructs timelines.
     * @param baseTimelineCount The original timeline count.
     * @return The modified timeline count.
     */
    default int modifyTimelineCount(AbstractCard constructionCard, int baseTimelineCount) {
        return baseTimelineCount;
    }

    /**
     * Modify the percentage health cost of timeline construction.
     * Note that this method is called before the timeline is constructed.
     * Hence, the construction may fail after this method is called.
     *
     * @param constructionCard     The card that constructed this timeline.
     * @param healthPercentageCost The current health cost in percentage.
     * @return The new health cost.
     */
    default float modifyTimelineHP(AbstractCard constructionCard, float healthPercentageCost) {
        return healthPercentageCost;
    }

    /**
     * Triggers after a timeline is successfully constructed.
     *
     * @param timeline The newly constructed timeline.
     */
    default void afterTimelineConstruct(TimelineMinion timeline) {

    }
}
