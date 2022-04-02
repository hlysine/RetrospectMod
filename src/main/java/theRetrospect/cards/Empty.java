package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Empty extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Empty.class.getSimpleName());

    public static final String IMG = makeCardPath("empty.png");

    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.STATUS;

    private static final int BASE_COST = 1;

    public Empty() {
        super(ID, IMG, BASE_COST, TYPE, COLOR, RARITY, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
