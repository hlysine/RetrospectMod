package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.CollapseTimelineAction;
import theRetrospect.actions.TriggerTimelineAction;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;
import theRetrospect.util.TimelineTargeting;

import static theRetrospect.RetrospectMod.makeCardPath;

public class IntoTheVoid extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("into_the_void.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = TimelineTargeting.TIMELINE;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_TRIGGER_COUNT = 1;
    private static final int UPGRADE_TRIGGER_COUNT = 1;

    private static final int COST = 1;

    public IntoTheVoid() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_TRIGGER_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TimelineMinion target = (TimelineMinion) TimelineTargeting.getTarget(this);

        addToBot(new TriggerTimelineAction(target, magicNumber, false));
        addToBot(new CollapseTimelineAction(target));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) return false;
        return MinionUtils.getMinions(p).monsters.size() != 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_TRIGGER_COUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}