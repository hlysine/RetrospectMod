package theRetrospect.util;


import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class CardFollowUpAction {
    public final AbstractGameAction action;
    /**
     * Whether to queue this action to the top or bottom of action manager queue
     */
    public final boolean onTop;
    /**
     * Whether this action should be skipped if the player's turn has already ended
     */
    public final boolean skipIfEndTurn;

    public CardFollowUpAction(AbstractGameAction action, boolean onTop, boolean skipIfEndTurn) {
        this.action = action;
        this.onTop = onTop;
        this.skipIfEndTurn = skipIfEndTurn;
    }
}
