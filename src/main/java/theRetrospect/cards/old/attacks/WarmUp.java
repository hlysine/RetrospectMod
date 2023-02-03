package theRetrospect.cards.old.attacks;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.WarmUpPower;

public class WarmUp extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(WarmUp.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public WarmUp() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageCallbackAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                damageDone -> {
                    if (damageDone > 0) {
                        addToBot(new ApplyPowerAction(p, p, new WarmUpPower(p, damageDone), damageDone));
                    }
                }
        ));
    }
}
