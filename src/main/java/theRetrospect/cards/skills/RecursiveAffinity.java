package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.util.CardPlaySource;

public class RecursiveAffinity extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(RecursiveAffinity.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public RecursiveAffinity() {
        super(ID, TARGET);

        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cards = info.getBaseValue("cards");
        addToBot(new ReplayLastCardsAction(card -> card != this, cards, CardPlaySource.CARD));
    }
}