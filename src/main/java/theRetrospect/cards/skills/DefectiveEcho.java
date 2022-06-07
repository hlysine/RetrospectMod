package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.DefectiveEchoPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class DefectiveEcho extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(DefectiveEcho.class.getSimpleName());
    public static final String IMG = makeCardPath("defective_echo.png");


    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int BASE_CARD_COUNT = 2;
    private static final int UPGRADE_CARD_COUNT = 1;

    public DefectiveEcho() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_CARD_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DefectiveEchoPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CARD_COUNT);
            initializeDescription();
        }
    }
}
