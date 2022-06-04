package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.effects.TimelineCollapseEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineCollapseSubscriber;

public class CollapseTimelineAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(CollapseTimelineAction.class.getName());

    private final AbstractFriendlyMonster minion;
    private boolean firstFrame = true;

    public CollapseTimelineAction(AbstractFriendlyMonster minion) {
        this.minion = minion;
    }

    @Override
    public void update() {
        if (minion.isDead) {
            if (firstFrame)
                logger.warn("Timeline is already dead before collapsing");
            this.isDone = true;
            return;
        }
        if (firstFrame) {
            CardCrawlGame.sound.playA("STANCE_ENTER_CALM", 0.1f);
            addToTop(new RepositionTimelinesAction());
            minion.die(false);
            AbstractDungeon.effectsQueue.add(new TimelineCollapseEffect(minion));
            addToTop(new NonTriggeringHealthChange(AbstractDungeon.player, minion.currentHealth));
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof TimelineCollapseSubscriber) {
                    TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) relic;
                    listener.onTimelineCollapse((TimelineMinion) minion);
                }
            }
            firstFrame = false;
        }
    }
}
