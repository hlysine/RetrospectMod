package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class DeviatedBurst extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(DeviatedBurst.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public DeviatedBurst() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < 3; i++) {
            this.damage = this.baseDamage = AbstractDungeon.cardRandomRng.random(this.magicNumber);
            this.applyPowers();
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }
}
