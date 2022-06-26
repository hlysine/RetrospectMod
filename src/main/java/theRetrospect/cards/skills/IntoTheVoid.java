package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.IntoTheVoidAction;
import theRetrospect.cards.AbstractBaseCard;

public class IntoTheVoid extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public IntoTheVoid() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new IntoTheVoidAction(magicNumber));
    }
}
