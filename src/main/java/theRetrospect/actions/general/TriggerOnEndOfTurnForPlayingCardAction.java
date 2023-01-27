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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * Trigger onEndOfTurnForPlayingCard for player powers, relics and minions
 */
public class TriggerOnEndOfTurnForPlayingCardAction extends AbstractGameAction {

    private <T> BiConsumer<T, Runnable> createLoopBody(AtomicBoolean skipWait) {
        return (item, next) -> {
            if (item instanceof EndOfTurnCardSubscriber) {
                EndOfTurnCardSubscriber subscriber = (EndOfTurnCardSubscriber) item;
                subscriber.triggerOnEndOfTurnForPlayingCard(() -> {
                    addToBot(new WaitAction(1f));
                    skipWait.set(false);
                    next.run();
                });
            } else {
                next.run();
            }
        };
    }

    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        AtomicBoolean skipWait = new AtomicBoolean(true);

        CallbackUtils.ForEachLoop(player.powers, createLoopBody(skipWait), () -> {
            if (!skipWait.get())
                addToBot(new WaitAction(1f));
            skipWait.set(true);
            CallbackUtils.ForEachLoop(player.relics, createLoopBody(skipWait), () -> {
                if (!skipWait.get())
                    addToBot(new WaitAction(1f));
                MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
                for (AbstractMonster monster : minions.monsters) {
                    if (monster instanceof TimelineMinion) {
                        TimelineMinion timeline = (TimelineMinion) monster;
                        if (!timeline.isDeadOrEscaped())
                            timeline.inTurn = true;
                    }
                }
                CallbackUtils.ForEachLoop(minions.monsters, (monster, next) -> {
                    if (monster instanceof AbstractMinionWithCards && !monster.isDeadOrEscaped()) {
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
        });
        this.isDone = true;
    }
}