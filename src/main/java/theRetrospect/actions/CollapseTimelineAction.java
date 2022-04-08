package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.effects.TimelineCollapseEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineCollapseListener;

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
            if (relic instanceof TimelineCollapseListener) {
                TimelineCollapseListener listener = (TimelineCollapseListener) relic;
                listener.onTimelineCollapse((TimelineMinion) minion);
            }
        }
        this.isDone = true;
    }
}
