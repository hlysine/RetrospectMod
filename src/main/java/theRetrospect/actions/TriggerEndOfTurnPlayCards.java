package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.util.MinionUtils;

public class TriggerEndOfTurnPlayCards extends AbstractGameAction {
    public void update() {
        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
        for (AbstractMonster monster : minions.monsters) {
            if (monster instanceof AbstractMinionWithCards) {
                AbstractMinionWithCards minion = (AbstractMinionWithCards) monster;
                minion.endOfTurnPlayCards();
            }
        }
        this.isDone = true;
    }
}