package theRetrospect.actions.universal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.mechanics.timetravel.TimeManager;
import theRetrospect.mechanics.timetravel.TimeTree;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MonsterUtils;
import theRetrospect.util.TimelineUtils;

import java.util.UUID;

public abstract class AbstractUniversalAction extends AbstractGameAction {

    public final AbstractMonster target;

    protected AbstractUniversalAction(AbstractMonster target) {
        this.target = target;
    }

    @Override
    public void update() {
        this.initialApplication();
        isDone = true;
    }

    public void initialApplication() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = target;
        TimeManager.timeTree.getActiveNode().universalActions.add(this);
        applyToCurrent(p, m);
        for (TimelineMinion timeline : TimelineUtils.getTimelines(p)) {
            applyToNode(timeline.getCurrentNode(), p, m != null ? MonsterUtils.getUuid(m) : null);
        }
    }

    public void applyToCurrent() {
        this.applyToCurrent(AbstractDungeon.player, target);
    }

    protected abstract void applyToCurrent(AbstractPlayer p, AbstractMonster m);

    protected abstract void applyToNode(TimeTree.Node node, AbstractPlayer p, UUID monsterTarget);

    public Power getPowerForTimeline(TimelineMinion timeline) {
        return getTimelinePower(timeline, AbstractDungeon.player, target != null ? MonsterUtils.getUuid(target) : null);
    }

    protected abstract Power getTimelinePower(TimelineMinion timeline, AbstractPlayer p, UUID monsterTarget);

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    /**
     * A marker class for powers that are only used to indicate a universal action on a timeline.
     */
    public static abstract class Power extends AbstractPower {

    }
}
