package theRetrospect.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.AbstractDrawable;

public class CardDrawable extends AbstractDrawable {
    private final AbstractCard card;

    public CardDrawable(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void render(SpriteBatch sb) {
        card.render(sb);
    }
}
