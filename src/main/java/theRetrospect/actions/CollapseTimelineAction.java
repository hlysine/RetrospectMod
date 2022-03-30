package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

public class CollapseTimelineAction extends AbstractGameAction {

    private final AbstractFriendlyMonster minion;

    public CollapseTimelineAction(AbstractFriendlyMonster minion) {
        this.minion = minion;
    }

    @Override
    public void update() {
        addToBot(new NonTriggeringHealthChange(AbstractDungeon.player, minion.currentHealth));
        addToBot(new InstantKillAction(minion));
        this.isDone = true;
    }
}
