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

public class AllOutAttack extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(AllOutAttack.class.getSimpleName());

    public static final String IMG = makeCardPath("all_out_attack.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int WEAK = 1;

    public AllOutAttack() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = WEAK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToTop(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, true), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}
