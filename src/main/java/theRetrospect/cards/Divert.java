package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ConstructTimelineAction;
import theRetrospect.util.MinionUtils;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Divert extends AbstractRetrospectCard {

    // TEXT DECLARATION

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_COST = 2;
    private static final int HEALTH_COST = 20;

    // /STAT DECLARATION/

    public Divert() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public boolean isReplayable() {
        return false;
    }

    // todo: check HP
    // todo: check max minion count

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructTimelineAction(HEALTH_COST));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) return false;

        if (p.currentHealth <= HEALTH_COST) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }

        if (MinionUtils.getMinions(p).monsters.size() >= MinionUtils.getMaxMinions(p)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
            return false;
        }

        return true;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
            this.initializeDescription();
        }
    }
}