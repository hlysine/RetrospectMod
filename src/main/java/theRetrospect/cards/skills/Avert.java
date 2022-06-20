package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.CustomQueueCardAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

public class Avert extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Avert.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Avert() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean cardPlayed = false;
        if (p.discardPile.size() > 0) {
            AbstractCard card = p.discardPile.getRandomCard(CardType.ATTACK, true);
            if (card != null) {
                p.discardPile.removeCard(card);
                CardUtils.addFollowUpActionToTop(card, new ConstructMultipleTimelineAction(this, timelineCount), false);
                addToBot(new ShowCardToBePlayedAction(card));
                addToBot(new CustomQueueCardAction(card, true, true, true));
                cardPlayed = true;
            }
        }
        if (!cardPlayed) {
            addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
        }
    }
}