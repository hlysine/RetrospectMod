package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class ElectricShock extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(ElectricShock.class.getSimpleName());

    public static final String IMG = makeCardPath("electric_shock.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int BASE_WEAK = 2;
    private static final int UPGRADE_WEAK = 1;

    public ElectricShock() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_WEAK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
        addToTop(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_WEAK);
            initializeDescription();
        }
    }
}
