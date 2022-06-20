package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class HatTrick extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(HatTrick.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public HatTrick() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterDrawPileToHandAction(magicNumber));
    }
}