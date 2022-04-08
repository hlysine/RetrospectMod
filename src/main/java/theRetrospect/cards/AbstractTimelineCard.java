package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.util.MinionUtils;

public abstract class AbstractTimelineCard extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(AbstractTimelineCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public int timelineCount;
    public int baseTimelineCount;
    public boolean upgradedTimelineCount;
    public boolean isTimelineCountModified;

    public AbstractTimelineCard(final String id,
                                final String img,
                                final int cost,
                                final CardType type,
                                final CardRarity rarity,
                                final CardTarget target) {

        super(id, img, cost, type, rarity, target);

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

    public boolean isReplayable() {
        return false;
    }
}