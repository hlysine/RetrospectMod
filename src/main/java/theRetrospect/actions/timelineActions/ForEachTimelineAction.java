package theRetrospect.actions.timelineActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.minions.TimelineMinion;

import java.util.function.Consumer;

public class ForEachTimelineAction extends AbstractGameAction {
    private final Consumer<TimelineMinion> action;

    public ForEachTimelineAction(Consumer<TimelineMinion> action) {
        this.action = action;
    }

    @Override
    public void update() {
        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
        for (AbstractMonster monster : minions.monsters) {
            if (monster instanceof TimelineMinion) {
                action.accept((TimelineMinion) monster);
            }
        }

        this.isDone = true;
    }
}
