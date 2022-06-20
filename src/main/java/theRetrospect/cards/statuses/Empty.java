package theRetrospect.cards.statuses;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class Empty extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Empty.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Empty() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUpgrade() {
        return !upgraded;
    }
}
