package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.CallbackUtils;

/**
 * Trigger onEndOfTurnForPlayingCard for player powers and minions
 */
public class TriggerOnEndOfTurnForPlayingCard extends AbstractGameAction {
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        CallbackUtils.ForEachLoop(player.powers, (power, next) -> {
            if (power instanceof EndOfTurnCardSubscriber) {
                EndOfTurnCardSubscriber cardPower = (EndOfTurnCardSubscriber) power;
                cardPower.triggerOnEndOfTurnForPlayingCard(() -> {
                    addToBot(new WaitAction(1f));
                    next.run();
                });
            } else {
                next.run();
            }
        }, () -> {
            addToBot(new WaitAction(1f));
            MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
            CallbackUtils.ForEachLoop(minions.monsters, (monster, next) -> {
                if (monster instanceof AbstractMinionWithCards) {
                    AbstractMinionWithCards minion = (AbstractMinionWithCards) monster;
                    minion.triggerOnEndOfTurnForPlayingCard(() -> {
                        addToBot(new WaitAction(1f));
                        next.run();
                    });
                } else {
                    next.run();
                }
            });
        });
        this.isDone = true;
    }
}