package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitForDeathAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(WaitForDeathAction.class.getName());

    public WaitForDeathAction(AbstractCreature target) {
        this.target = target;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (!this.target.isDying) {
            logger.warn("Target is not dying, exiting to prevent soft lock.");
            this.isDone = true;
            return;
        }
        if (this.target.isDead) {
            this.isDone = true;
        }
    }
}
