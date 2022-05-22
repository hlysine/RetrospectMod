package theRetrospect.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ConstructTimelineAction;

import static theRetrospect.RetrospectMod.makeCardPath;

public class UnstableChronology extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(UnstableChronology.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("unstable_chronology.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 4;

    public UnstableChronology() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        timelineCount = baseTimelineCount = 1;
    }

    @Override
    public void tookDamage() {
        updateCost(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructTimelineAction(this));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (this.cost < 4) {
                upgradeBaseCost(this.cost - 1);
                if (this.cost < 0) {
                    this.cost = 0;
                }
            } else {
                upgradeBaseCost(3);
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard tmp = new UnstableChronology();
        if (AbstractDungeon.player != null) {
            tmp.updateCost(-AbstractDungeon.player.damagedThisCombat);
        }
        return tmp;
    }
}