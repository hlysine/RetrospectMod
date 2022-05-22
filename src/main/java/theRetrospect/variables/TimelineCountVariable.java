package theRetrospect.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeID;

public class TimelineCountVariable extends DynamicVariable {

    @Override
    public String key() {
        return makeID("TimelineCount");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractRetrospectCard) card).isTimelineCountModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractRetrospectCard) card).timelineCount;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractRetrospectCard) card).baseTimelineCount;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractRetrospectCard) card).upgradedTimelineCount;
    }
}