package theRetrospect.actions.timelineActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.util.TimelineUtils;

public class CollapseTimelineAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(CollapseTimelineAction.class.getName());

    private final AbstractFriendlyMonster minion;

    public CollapseTimelineAction(AbstractFriendlyMonster minion) {
        this.minion = minion;
    }

    @Override
    public void update() {
        if (minion.isDeadOrEscaped()) {
            logger.warn("Timeline is already dead before collapsing");
            this.isDone = true;
            return;
        }

        if (!TimelineUtils.notifyCollapse(minion)) {
            this.isDone = true;
            return;
        }
        TimelineUtils.queuedCollapse(minion);

        this.isDone = true;
    }
}
