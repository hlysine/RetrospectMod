package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ConstructTimelineAction;
import theRetrospect.powers.FrozenPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Overcore extends AbstractTimelineCard {

    public static final String ID = RetrospectMod.makeID(Overcore.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_COST = 1;
    private static final int BASE_TIMELINE_COUNT = 3;
    private static final int BASE_FROZEN_COUNT = 2;
    private static final int UPGRADE_FROZEN = -1;

    public Overcore() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        this.baseTimelineCount = BASE_TIMELINE_COUNT;
        this.timelineCount = this.baseTimelineCount;
        this.baseMagicNumber = BASE_FROZEN_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < baseTimelineCount; i++)
            addToBot(new ConstructTimelineAction(healthCostPerTimeline));
        addToBot(new ApplyPowerAction(p, p, new FrozenPower(p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FROZEN);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}