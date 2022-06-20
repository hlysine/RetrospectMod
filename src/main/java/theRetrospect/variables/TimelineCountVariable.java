package theRetrospect.variables;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.cards.AbstractBaseCard;

import static theRetrospect.RetrospectMod.makeID;

public class TimelineCountVariable extends CustomVariable {

    @Override
    public String key() {
        return makeID("TimelineCount");
    }

    @Override
    public void setBaseValue(AbstractCard card, int value) {
        if (card instanceof AbstractBaseCard) {
            AbstractBaseCard rCard = (AbstractBaseCard) card;
            rCard.baseTimelineCount = rCard.timelineCount = value;
        }
    }

    @Override
    public void upgradeValue(AbstractCard card, int upgradeValue) {
        if (card instanceof AbstractBaseCard) {
            AbstractBaseCard rCard = (AbstractBaseCard) card;
            rCard.upgradeTimelineCount(upgradeValue);
        }
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractBaseCard) card).isTimelineCountModified;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        ((AbstractBaseCard) card).isTimelineCountModified = false;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractBaseCard) card).timelineCount;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractBaseCard) card).baseTimelineCount;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractBaseCard) card).upgradedTimelineCount;
    }
}