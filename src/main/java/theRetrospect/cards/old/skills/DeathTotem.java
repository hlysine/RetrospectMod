package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.DeathTotemPower;

public class DeathTotem extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(DeathTotem.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public DeathTotem() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, (int) (p.currentHealth * magicNumber / 100f)));
        addToBot(new ApplyPowerAction(p, p, new DeathTotemPower(p, 1)));
    }
}