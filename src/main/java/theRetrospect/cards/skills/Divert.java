package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.RewindAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.VolatileEnergyPower;

public class Divert extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Divert() {
        super(ID, TARGET);

        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RewindAction(this, timelineCount));
        addToBot(new ApplyPowerAction(p, p, new VolatileEnergyPower(p, 10)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.isEthereal = false;
        }
        super.upgrade();
    }
}