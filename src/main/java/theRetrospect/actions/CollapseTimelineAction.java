package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.effects.TimelineCollapseEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineCollapseSubscriber;

public class CollapseTimelineAction extends AbstractGameAction {

    private final AbstractFriendlyMonster minion;

    public CollapseTimelineAction(AbstractFriendlyMonster minion) {
        this.minion = minion;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playA("STANCE_ENTER_CALM", 0.1f);
        addToTop(new RepositionTimelinesAction());
        addToTop(new InstantKillAction(minion));
        addToTop(new VFXAction(new TimelineCollapseEffect(minion)));
        addToTop(new NonTriggeringHealthChange(AbstractDungeon.player, minion.currentHealth));
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) relic;
                listener.onTimelineCollapse((TimelineMinion) minion);
            }
        }
        this.isDone = true;
    }
}
