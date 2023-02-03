package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.DefectiveEchoPower;

public class DefectiveEcho extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(DefectiveEcho.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public DefectiveEcho() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DefectiveEchoPower(p, magicNumber), magicNumber));
    }
}
