package theRetrospect.actions.timelineActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ConstructMultipleTimelineAction extends AbstractGameAction {

    private final AbstractCard constructionCard;

    public ConstructMultipleTimelineAction(AbstractCard constructionCard, int amount) {
        this.constructionCard = constructionCard;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (int i = 0; i < amount; i++) {
            addToTop(new ConstructTimelineAction(constructionCard));
        }
        this.isDone = true;
    }
}
