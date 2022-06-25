package theRetrospect.util;


import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class CardFollowUpAction implements Comparable<CardFollowUpAction> {
    public final AbstractGameAction action;
    /**
     * Whether to queue this action to the top or bottom of action manager queue
     */
    public final boolean onTop;
    /**
     * Whether this action should be skipped if the player's turn has already ended
     */
    public final boolean skipIfEndTurn;
    /**
     * Actions with a higher priority will be enqueued in a way that results in a smaller index in the queue.
     * If actions have the same priority, those added last implicitly have a higher priority.
     */
    public final int priority;

    public CardFollowUpAction(AbstractGameAction action, boolean onTop, boolean skipIfEndTurn, int priority) {
        this.action = action;
        this.onTop = onTop;
        this.skipIfEndTurn = skipIfEndTurn;
        this.priority = priority;
    }

    @Override
    public int compareTo(CardFollowUpAction o) {
        return this.priority - o.priority;
    }
}
