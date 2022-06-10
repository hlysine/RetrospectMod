package theRetrospect.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.RetrospectMod;
import theRetrospect.characters.TheRetrospect;

public abstract class AbstractRetrospectCard extends AbstractBaseCard {

    public static final CardColor COLOR = TheRetrospect.Enums.RETROSPECT_CARD_VIOLET;

    public static final String ID = RetrospectMod.makeID(AbstractRetrospectCard.class.getSimpleName());

    public int timelineCount;
    public int baseTimelineCount;
    public boolean upgradedTimelineCount;
    public boolean isTimelineCountModified;

    public AbstractRetrospectCard(final String id,
                                  final String img,
                                  final int cost,
                                  final CardType type,
                                  final CardRarity rarity,
                                  final CardTarget target) {

        super(id, img, cost, type, COLOR, rarity, target);

        isTimelineCountModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedTimelineCount) {
            timelineCount = baseTimelineCount;
            isTimelineCountModified = true;
        }
    }

    public void upgradeTimelineCount(int amount) {
        baseTimelineCount += amount;
        timelineCount = baseTimelineCount;
        upgradedTimelineCount = true;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractRetrospectCard copy = (AbstractRetrospectCard) super.makeStatEquivalentCopy();
        copy.baseTimelineCount = this.baseTimelineCount;
        copy.isTimelineCountModified = this.isTimelineCountModified;
        copy.timelineCount = this.timelineCount;
        copy.upgradedTimelineCount = this.upgradedTimelineCount;
        return copy;
    }
}