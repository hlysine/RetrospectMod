package theRetrospect.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HoverableCardStack {
    private static final float HOVERED_CARD_MARGIN = 20f;
    private static final float SPACING = 30;
    private static final float HOVERED_SCALE = 1f;
    private static final float UNHOVERED_SCALE = 0.3f;

    public final List<AbstractCard> cards;
    public float x;
    public float y;

    private final Map<AbstractCard, Boolean> cardHovered = new HashMap<>();

    public HoverableCardStack(List<AbstractCard> cards, float x, float y) {
        this.cards = cards;
        this.x = x;
        this.y = y;
        resetAllCardPositions();
    }

    public void update() {
        cardHovered.clear();
        for (AbstractCard card : cards) {
            cardHovered.put(card, CardUtils.getHovered(card));
        }

        Optional<Map.Entry<AbstractCard, Boolean>> hoveredCard = cardHovered.entrySet().stream().filter(Map.Entry::getValue).findFirst();
        if (hoveredCard.isPresent()) {
            AbstractCard card = hoveredCard.get().getKey();
            card.updateHoverLogic();
            if (card.hb.hovered) {
                card.drawScale = HOVERED_SCALE;
                card.targetDrawScale = HOVERED_SCALE;
            } else {
                resetAllCardPositions();
            }
        } else {
            int hoverIndex = -1;
            for (int i = 0; i < cards.size(); i++) {
                AbstractCard card = cards.get(i);
                if (hoverIndex == -1)
                    card.updateHoverLogic();
                if (hoverIndex == -1 && card.hb.hovered) {
                    card.drawScale = HOVERED_SCALE;
                    card.targetDrawScale = HOVERED_SCALE;
                    hoverIndex = i;
                } else {
                    card.targetDrawScale = UNHOVERED_SCALE;
                }
            }
            if (hoverIndex == -1) {
                resetAllCardPositions();
            } else {
                setCardPosition(hoverIndex);
                AbstractCard newHoveredCard = cards.get(hoverIndex);
                newHoveredCard.update();
                for (int i = 0; i < cards.size(); i++) {
                    AbstractCard card = cards.get(i);
                    if (i != hoverIndex) {
                        if (i < hoverIndex) {
                            card.target_x = newHoveredCard.target_x + newHoveredCard.hb.width / 2 + SPACING * (hoverIndex - i) + HOVERED_CARD_MARGIN;
                        } else {
                            card.target_x = newHoveredCard.target_x - newHoveredCard.hb.width / 2 - SPACING * (i - hoverIndex) - HOVERED_CARD_MARGIN;
                        }
                        card.target_y = this.y;
                    }
                }
            }
        }

        for (AbstractCard card : cards) {
            card.update();
        }
    }

    private void resetAllCardPositions() {
        for (int i = 0; i < cards.size(); i++) {
            setCardPosition(i);
        }
    }

    private void setCardPosition(int cardIndex) {
        float offsetX = (cards.size() - 1) * SPACING / 2;
        AbstractCard card = cards.get(cardIndex);
        card.target_x = this.x + offsetX - SPACING * cardIndex;
        card.target_y = this.y;
    }

    public void render(SpriteBatch sb) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            AbstractCard card = cards.get(i);
            card.render(sb);
            if (card.hb.hovered) {
                card.renderCardTip(sb);
            }
        }
    }
}
