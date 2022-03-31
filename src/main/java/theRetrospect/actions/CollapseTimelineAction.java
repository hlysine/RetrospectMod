package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.effects.TimelineCollapseEffect;
import theRetrospect.minions.TimelineMinion;

public class CollapseTimelineAction extends AbstractGameAction {

    private final AbstractFriendlyMonster minion;

    public CollapseTimelineAction(AbstractFriendlyMonster minion) {
        this.minion = minion;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playA("STANCE_ENTER_CALM", 0.1f);
        AbstractDungeon.effectsQueue.add(new TimelineCollapseEffect(minion));
        addToBot(new NonTriggeringHealthChange(AbstractDungeon.player, minion.currentHealth));
        addToBot(new InstantKillAction(minion));
        addToBot(new RepositionTimelinesAction());
        this.isDone = true;
    }
}
