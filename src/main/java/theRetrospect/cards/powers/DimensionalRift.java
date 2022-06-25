package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.DimensionalRiftPower;

public class DimensionalRift extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(DimensionalRift.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public DimensionalRift() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DimensionalRiftPower(p)));
    }
}
