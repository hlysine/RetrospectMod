package theRetrospect.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.patches.CardAddFieldsPatch;

import java.lang.reflect.Field;
import java.util.List;

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
        return CardAddFieldsPatch.isBeingReplayed.get(card);
    }

    public static void setIsBeingReplayed(AbstractCard card, boolean newVal) {
        CardAddFieldsPatch.isBeingReplayed.set(card, newVal);
    }

    public static List<Runnable> getActionsAfterUse(AbstractCard card) {
        return CardAddFieldsPatch.actionAfterUse.get(card);
    }

    /**
     * Remove all actions from the card.
     *
     * @param card The card to remove actions from.
     */
    public static void clearActionsAfterUse(AbstractCard card) {
        CardAddFieldsPatch.actionAfterUse.get(card).clear();
    }

    /**
     * Set an action to be executed after the card is played and all the onAfterCardUse triggers are complete.
     * The action is only executed once and is cleared afterwards.
     * Note that if dontTriggerAfterUse is true, the action is cleared without being executed.
     *
     * @param card           The card to store the action in.
     * @param actionAfterUse The action to be executed.
     */
    public static void addActionAfterUse(AbstractCard card, Runnable actionAfterUse) {
        CardAddFieldsPatch.actionAfterUse.get(card).add(actionAfterUse);
    }

    /**
     * Remove an existing action. Does nothing if the action does not exist on the card.
     *
     * @param card           The card to remove the action from.
     * @param actionAfterUse The action to be removed.
     * @return True if the action was in the card.
     */
    public static boolean removeActionAfterUse(AbstractCard card, Runnable actionAfterUse) {
        return CardAddFieldsPatch.actionAfterUse.get(card).remove(actionAfterUse);
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

    public static int calculateXCostEffect(AbstractCard card) {
        int effect = EnergyPanel.totalCount;
        if (card.energyOnUse != -1) {
            effect = card.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }

        return effect;
    }
}
