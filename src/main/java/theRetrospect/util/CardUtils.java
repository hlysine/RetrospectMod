package theRetrospect.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
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
}
