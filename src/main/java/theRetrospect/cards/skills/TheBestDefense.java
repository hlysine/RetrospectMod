package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.TheBestDefenseAction;
import theRetrospect.cards.AbstractBaseCard;

public class TheBestDefense extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(TheBestDefense.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public TheBestDefense() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TheBestDefenseAction(block));
    }
}