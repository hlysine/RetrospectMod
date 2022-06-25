package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.AvertAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

public class Avert extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Avert.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Avert() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardUtils.addFollowUpActionToBottom(this, new AvertAction(this), true, 0);
    }
}