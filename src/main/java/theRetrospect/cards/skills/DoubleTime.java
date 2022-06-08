package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.CollapseTimelineAction;
import theRetrospect.actions.timelineActions.TriggerTimelineAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

import static theRetrospect.RetrospectMod.makeCardPath;

public class DoubleTime extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(DoubleTime.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("double_time.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_COST = 1;

    public DoubleTime() {
        super(ID, IMG, BASE_COST, TYPE, RARITY);
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new TriggerTimelineAction(target, 1, true, new CollapseTimelineAction(target)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.initializeDescription();
        }
    }
}