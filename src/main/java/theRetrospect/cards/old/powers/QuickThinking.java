package theRetrospect.cards.old.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class QuickThinking extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(QuickThinking.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public QuickThinking() {
        super(ID, TARGET);

        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DrawPower(p, magicNumber)));
    }
}
