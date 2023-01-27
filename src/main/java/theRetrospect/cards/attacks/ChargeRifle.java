package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.ChargeRifleAction;
import theRetrospect.cards.AbstractBaseCard;

public class ChargeRifle extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ChargeRifle.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChargeRifle() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ChargeRifleAction(this, this.magicNumber));
    }
}
