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

    public static final int DEFAULT_TIMELINE_HEALTH_COST = 20;

    public int timelineCount;
    public int baseTimelineCount;
    public boolean upgradedTimelineCount;
    public boolean isTimelineCountModified;

    protected int healthCostPerTimeline;

    public AbstractTimelineCard(final String id,
                                final String img,
                                final int cost,
                                final CardType type,
                                final CardRarity rarity,
                                final CardTarget target) {

        super(id, img, cost, type, rarity, target);
        this.healthCostPerTimeline = DEFAULT_TIMELINE_HEALTH_COST;

        isTimelineCountModified = false;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) return false;

        if (p.currentHealth <= healthCostPerTimeline * timelineCount) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }

        if (MinionUtils.getMinions(p).monsters.size() + timelineCount > MinionUtils.getMaxMinions(p)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
            return false;
        }

        return true;
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