package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class NoxiousWraith extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(NoxiousWraith.class.getSimpleName());
    public static final String IMG = makeCardPath("noxious_wraith.png");


    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int INTANGIBLE = 2;
    private static final int BASE_DEXTERITY_LOSS = 5;
    private static final int UPGRADE_DEXTERITY_LOSS = -2;

    public NoxiousWraith() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_DEXTERITY_LOSS;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, INTANGIBLE), INTANGIBLE));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DEXTERITY_LOSS);
            initializeDescription();
        }
    }
}
