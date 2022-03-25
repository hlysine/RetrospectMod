package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import theRetrospect.minions.AbstractMinionWithCards;

public class TriggerEndOfTurnPlayCards extends AbstractGameAction {
    public void update() {
        if (AbstractDungeon.player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions player = (AbstractPlayerWithMinions) AbstractDungeon.player;
            for (AbstractMonster monster : player.minions.monsters) {
                if (monster instanceof AbstractMinionWithCards) {
                    AbstractMinionWithCards minion = (AbstractMinionWithCards) monster;
                    minion.endOfTurnPlayCards();
                }
            }
        }
        this.isDone = true;
    }
}