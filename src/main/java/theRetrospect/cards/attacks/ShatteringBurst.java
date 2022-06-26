package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.ShatteringBurstAction;
import theRetrospect.cards.AbstractBaseCard;

public class ShatteringBurst extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ShatteringBurst.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public ShatteringBurst() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ShatteringBurstAction(magicNumber, p, damage, damageTypeForTurn));
    }
}
