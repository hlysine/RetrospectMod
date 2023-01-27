package theRetrospect.mechanics.card;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Schedules all follow-up actions of a card.
 */
public class CardFollowUpActionHandler {
    private final List<CardFollowUpAction> followUpActions;
    private final boolean turnEnding;

    public CardFollowUpActionHandler(List<CardFollowUpAction> followUpActions, boolean turnEnding) {
        this.followUpActions = new ArrayList<>(followUpActions);
        this.turnEnding = turnEnding;
    }

    public void scheduleFollowUpActions() {

        List<CardFollowUpAction> followUpActions = this.followUpActions;
        boolean turnEnding = this.turnEnding;

        // For actions added to the top of the queue, add them in ascending priority
        // so that actions with the highest priority gets added last, achieving first place in queue.
        List<CardFollowUpAction> topItems = followUpActions.stream()
                .filter(item -> item.onTop)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        for (CardFollowUpAction item : topItems) {
            if (turnEnding && item.skipIfEndTurn)
                continue;
            AbstractDungeon.actionManager.addToTop(item.action);
        }

        // For actions added to the bottom of the queue, add them in descending priority
        // so that actions with the lowest priority gets added last, achieving last place in queue.

        // Reverse the list first, so that items added last get a higher priority implicitly.
        // Relies on the fact that sorted() is stable.
        Deque<CardFollowUpAction> bottomItemsReversed = new ArrayDeque<>();
        for (CardFollowUpAction item : followUpActions) {
            if (!item.onTop) {
                bottomItemsReversed.addFirst(item);
            }
        }
        List<CardFollowUpAction> bottomItems = bottomItemsReversed.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        for (CardFollowUpAction item : bottomItems) {
            if (turnEnding && item.skipIfEndTurn)
                continue;
            AbstractDungeon.actionManager.addToBottom(item.action);
        }
    }
}
