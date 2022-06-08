package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.EvanescencePower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Evanescence extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Evanescence.class.getSimpleName());
    public static final String IMG = makeCardPath("evanescence.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 2;
    private static final int BASE_BLOCK_GAIN = 3;
    private static final int UPGRADE_BLOCK_GAIN = 2;

    public Evanescence() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_BLOCK_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EvanescencePower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLOCK_GAIN);
            initializeDescription();
        }
    }
}
