package theRetrospect.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.patches.CardAddReplayFieldPatch;

import java.lang.reflect.Field;

public class CardUtils {
    private static final Field hoveredField;

    static {
        Field field;
        try {
            field = AbstractCard.class.getDeclaredField("hovered");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            field = null;
            e.printStackTrace();
        }
        hoveredField = field;
    }

    public static boolean getHovered(AbstractCard card) {
        try {
            return (boolean) hoveredField.get(card);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static boolean getIsBeingReplayed(AbstractCard card) {
        return CardAddReplayFieldPatch.isBeingReplayed.get(card);
    }

    public static void setIsBeingReplayed(AbstractCard card, boolean newVal) {
        CardAddReplayFieldPatch.isBeingReplayed.set(card, newVal);
    }

    public static boolean isCardReplayable(AbstractCard card) {
        return !(card instanceof AbstractRetrospectCard) || ((AbstractRetrospectCard) card).isReplayable();
    }

    /**
     * Make a copy of the card with the same stats, position and powers applied.
     *
     * @param card The card to be copied.
     * @return A new copy of the card.
     */
    public static AbstractCard makeAdvancedCopy(AbstractCard card) {
        AbstractCard newCard = card.makeStatEquivalentCopy();
        newCard.current_x = card.current_x;
        newCard.target_x = newCard.current_x;
        newCard.current_y = card.current_y;
        newCard.target_y = newCard.current_y;
        newCard.applyPowers();
        return newCard;
    }
}
