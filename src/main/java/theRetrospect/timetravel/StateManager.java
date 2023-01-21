package theRetrospect.timetravel;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.actions.general.RunnableAction;

public class StateManager {
    public static CombatStateTree stateTree;

    public static void reset() {
        stateTree = new CombatStateTree();
    }

    public static void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(() -> stateTree.addNode(GameActionManager.turn, CombatState.extractState())));
    }

    /**
     * Travel to a previous round. Present game state is saved as a mid-state of the current round while the previous round is restored.
     *
     * @param rounds Number of rounds to travel back.
     * @return A Linear Path with the origin at the present time.
     */
    public static CombatStateTree.LinearPath rewindTime(int rounds) {
        CombatState currentState = CombatState.extractState();
        stateTree.getActiveNode().midStates.add(currentState);
        CombatStateTree.LinearPath path = stateTree.createLinearPath(stateTree.getActiveNode(), currentState);
        stateTree.setActiveNode(path.getNodeForRound(stateTree.getCurrentRound() - rounds));
        stateTree.getActiveNode().baseState.restoreStateForRewind();
        return path;
    }
}
