package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import theRetrospect.minions.AbstractMinionWithCards;

public class TriggerEndOfTurnPlayCards extends AbstractGameAction {
    public void update() {
        MonsterGroup minions;
        if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
            minions = ((AbstractPlayerWithMinions) AbstractDungeon.player).minions;
        } else {
            minions = BasePlayerMinionHelper.getMinions(AbstractDungeon.player);
        }
        for (AbstractMonster monster : minions.monsters) {
            if (monster instanceof AbstractMinionWithCards) {
                AbstractMinionWithCards minion = (AbstractMinionWithCards) monster;
                minion.endOfTurnPlayCards();
            }
        }
        this.isDone = true;
    }
}