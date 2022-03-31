package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.util.EndOfTurnCardPlaying;
import theRetrospect.util.MinionUtils;

public class TriggerEndOfTurnPlayCards extends AbstractGameAction {
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        for (AbstractPower power : player.powers) {
            if (power instanceof EndOfTurnCardPlaying) {
                EndOfTurnCardPlaying cardPower = (EndOfTurnCardPlaying) power;
                cardPower.endOfTurnPlayCards();
            }
        }
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