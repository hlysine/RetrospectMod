package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class SoullessDemon extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(SoullessDemon.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public SoullessDemon() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 1), 1));
    }
}
