package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.ConvergeAction;
import theRetrospect.cards.AbstractBaseCard;

public class Converge extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Converge.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Converge() {
        super(ID, TARGET);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConvergeAction(1, magicNumber));
    }
}