package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.SynchronizedReflexPower;

public class SynchronizedReflex extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(SynchronizedReflex.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public SynchronizedReflex() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SynchronizedReflexPower(p, magicNumber)));
    }
}
