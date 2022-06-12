package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.minions.TimelineMinion;
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
            for (AbstractMonster monster : minions.monsters) {
                if (monster instanceof TimelineMinion) {
                    TimelineMinion timeline = (TimelineMinion) monster;
                    timeline.inTurn = true;
                }
            }
            CallbackUtils.ForEachLoop(minions.monsters, (monster, next) -> {
                if (monster instanceof AbstractMinionWithCards) {
                    AbstractMinionWithCards minion = (AbstractMinionWithCards) monster;
                    minion.triggerOnEndOfTurnForPlayingCard(() -> {
                        if (minion instanceof TimelineMinion) {
                            TimelineMinion timeline = (TimelineMinion) minion;
                            timeline.inTurn = false;
                        }
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