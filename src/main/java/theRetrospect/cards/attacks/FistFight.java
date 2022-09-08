package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class FistFight extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(FistFight.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public FistFight() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToTop(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, true), this.magicNumber));
    }
}
