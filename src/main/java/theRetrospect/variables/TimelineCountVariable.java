package theRetrospect.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.cards.AbstractTimelineCard;

import static theRetrospect.RetrospectMod.makeID;

public class TimelineCountVariable extends DynamicVariable {

    @Override
    public String key() {
        return makeID("TimelineCount");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractTimelineCard) card).isTimelineCountModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractTimelineCard) card).timelineCount;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractTimelineCard) card).baseTimelineCount;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractTimelineCard) card).upgradedDefaultSecondMagicNumber;
    }
}