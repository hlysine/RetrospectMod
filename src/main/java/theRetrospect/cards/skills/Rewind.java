package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

public class Rewind extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Rewind.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Rewind() {
        super(ID, TARGET);

        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardUtils.addFollowUpActionToBottom(this, new ReplayLastCardsAction(
                this,
                null,
                1,
                CardPlaySource.CARD,
                this.current_x,
                this.current_y
        ), true, 0);
    }
}
