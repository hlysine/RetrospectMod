package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.old.statuses.Paradox;
import theRetrospect.mechanics.card.CardPlaySource;
import theRetrospect.util.CardUtils;

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
        CardUtils.addFollowUpActionToBottom(this, new ReplayLastCardsAction(
                this,
                null,
                cards,
                CardPlaySource.CARD,
                this.current_x,
                this.current_y
        ), true, 0);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.selfRetain = true;
        }
        super.upgrade();
    }
}