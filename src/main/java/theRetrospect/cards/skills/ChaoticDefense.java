package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.ChaoticCardAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.util.CardUtils;

public class ChaoticDefense extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ChaoticDefense.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public ChaoticDefense() {
        super(ID, TARGET);

        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardUtils.addFollowUpActionToBottom(
                this,
                new ChaoticCardAction(this, p.drawPile, CardType.SKILL, CardUtils.createFollowUpActionHandler(this)),
                true,
                0
        );
    }
}