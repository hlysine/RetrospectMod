package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.FragmentationAction;
import theRetrospect.cards.AbstractBaseCard;

public class Fragmentation extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Fragmentation.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Fragmentation() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FragmentationAction(magicNumber, p, damage, damageTypeForTurn));
    }
}
