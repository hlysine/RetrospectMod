package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.IntoTheVoidPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class IntoTheVoid extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("into_the_void.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int BASE_HP_LOSS = 40;
    private static final int UPGRADE_HP_LOSS = -10;

    public IntoTheVoid() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, (int) (p.currentHealth * magicNumber / 100f)));
        addToBot(new ApplyPowerAction(p, p, new IntoTheVoidPower(p, 1)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HP_LOSS);
            this.initializeDescription();
        }
    }
}