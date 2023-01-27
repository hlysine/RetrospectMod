package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.TransmuteAction;
import theRetrospect.cards.AbstractBaseCard;

public class Transmute extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Transmute.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Transmute() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TransmuteAction(magicNumber, true));
    }
}