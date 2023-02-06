package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.universal.UniversalStrengthAction;
import theRetrospect.cards.AbstractBaseCard;

public class UniversalCharge extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(UniversalCharge.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public UniversalCharge() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new UniversalStrengthAction(magicNumber));
    }
}
