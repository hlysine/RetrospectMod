package theRetrospect.actions.general;

import com.badlogic.gdx.math.MathUtils;
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
        this.origin_x = MathUtils.clamp(origin_x, Settings.WIDTH * 0.3f, Settings.WIDTH * 0.7f);
        this.origin_y = MathUtils.clamp(origin_y, Settings.HEIGHT * 0.3f, Settings.HEIGHT * 0.7f);
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
