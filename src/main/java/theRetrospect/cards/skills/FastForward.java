package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.DiscardUpToAction;
import theRetrospect.cards.AbstractBaseCard;

public class FastForward extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(FastForward.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public FastForward() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new DiscardUpToAction(p, p, magicNumber, true));
    }
}