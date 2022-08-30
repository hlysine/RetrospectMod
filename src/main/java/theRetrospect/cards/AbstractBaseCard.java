package theRetrospect.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import hlysine.STSItemInfo.CardInfo;
import theRetrospect.RetrospectMod;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static theRetrospect.RetrospectMod.getCardInfo;
import static theRetrospect.RetrospectMod.makeCardPath;

public abstract class AbstractBaseCard extends CustomCard {

    public static final String ID = RetrospectMod.makeID(AbstractBaseCard.class.getSimpleName());

    protected final CardStrings cardStrings;
    protected final CardInfo info;

    public boolean delusional = false;
    public boolean paradoxical = false;

    public int timelineCount = 0;
    public int baseTimelineCount = 0;
    public boolean upgradedTimelineCount = false;
    public boolean isTimelineCountModified = false;

    public AbstractBaseCard(final String id, final CardTarget target) {
        super(
                id,
                languagePack.getCardStrings(id).NAME,
                makeCardPath(getCardInfo(id).getImage()),
                getCardInfo(id).getBaseCost(),
                languagePack.getCardStrings(id).DESCRIPTION,
                getCardInfo(id).getType(),
                getCardInfo(id).getColor(),
                getCardInfo(id).getRarity(),
                target
        );
        cardStrings = languagePack.getCardStrings(id);
        info = getCardInfo(id);

        info.setBaseValues(this);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (delusional)
            addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
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
        AbstractBaseCard copy = (AbstractBaseCard) super.makeStatEquivalentCopy();
        copy.baseTimelineCount = this.baseTimelineCount;
        copy.isTimelineCountModified = this.isTimelineCountModified;
        copy.timelineCount = this.timelineCount;
        copy.upgradedTimelineCount = this.upgradedTimelineCount;
        return copy;
    }

    protected void upgradeValues() {
        info.upgradeValues(this);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeValues();
            initializeDescription();
        }
    }
}