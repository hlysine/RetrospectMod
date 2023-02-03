package theRetrospect.cards.old.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class Ambush extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Ambush.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Ambush() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = p.drawPile.size();
        for (int i = 0; i < count; i += magicNumber) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
        }
    }
}
