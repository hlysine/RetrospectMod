package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import theRetrospect.effects.ShowCardToBePlayedEffect;

import java.util.Collections;
import java.util.List;

public class ShowCardToBePlayedAction extends AbstractGameAction {
    private final List<AbstractCard> cards;
    private final float origin_x;
    private final float origin_y;

    public ShowCardToBePlayedAction(AbstractCard card) {
        this(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f);
    }

    public ShowCardToBePlayedAction(AbstractCard card, float origin_x, float origin_y) {
        this(Collections.singletonList(card), origin_x, origin_y);
    }

    public ShowCardToBePlayedAction(List<AbstractCard> cards) {
        this(cards, Settings.WIDTH / 2f, Settings.HEIGHT / 2f);
    }

    public ShowCardToBePlayedAction(List<AbstractCard> card, float origin_x, float origin_y) {
        this.cards = card;
        this.origin_x = origin_x;
        this.origin_y = origin_y;
    }

    @Override
    public void update() {
        if (cards.size() == 0) {
            this.isDone = true;
            return;
        }
        addToTop(new VFXAction(null, new ShowCardToBePlayedEffect(cards, origin_x, origin_y), Settings.FAST_MODE ? 0.5F : 1.5F, true));

        this.isDone = true;
    }
}
