package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.actions.general.QueueCardIntentAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.cards.attacks.RecursionHell;
import theRetrospect.util.CardPlaySource;

public class RecursionHellAction extends AbstractGameAction {

    private final float origin_x;
    private final float origin_y;

    public RecursionHellAction(float origin_x, float origin_y) {
        this.origin_x = origin_x;
        this.origin_y = origin_y;
    }

    @Override
    public void update() {
        AbstractCard recursionCard = new RecursionHell();
        addToBot(new ShowCardToBePlayedAction(recursionCard, origin_x, origin_y));
        addToBot(new QueueCardIntentAction(recursionCard, null, CardPlaySource.CARD, true, true));
        this.isDone = true;
    }
}
