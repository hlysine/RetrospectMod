package theRetrospect.actions.timetravel;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theRetrospect.timetravel.CombatStateTree;
import theRetrospect.timetravel.StateManager;

public class RewindAction extends AbstractGameAction {

    private final int rounds;

    public RewindAction(int rounds) {
        this.rounds = rounds;
    }

    @Override
    public void update() {
        CombatStateTree.LinearPath path = StateManager.rewindTime(rounds);

        isDone = true;
    }
}
