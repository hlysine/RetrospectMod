package theRetrospect.variables;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeID;

public class TimelineCountVariable extends CustomVariable {

    @Override
    public String key() {
        return makeID("TimelineCount");
    }

    @Override
    public void setBaseValue(AbstractCard card, int value) {
        if (card instanceof AbstractRetrospectCard) {
            AbstractRetrospectCard rCard = (AbstractRetrospectCard) card;
            rCard.baseTimelineCount = rCard.timelineCount = value;
        }
    }

    @Override
    public void upgradeValue(AbstractCard card, int upgradeValue) {
        if (card instanceof AbstractRetrospectCard) {
            AbstractRetrospectCard rCard = (AbstractRetrospectCard) card;
            rCard.upgradeTimelineCount(upgradeValue);
        }
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractRetrospectCard) card).isTimelineCountModified;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        ((AbstractRetrospectCard) card).isTimelineCountModified = false;
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