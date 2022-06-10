package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Divert extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("divert.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;

    public Divert() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        this.isEthereal = true;
        timelineCount = baseTimelineCount = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isEthereal = false;
            this.initializeDescription();
        }
    }
}