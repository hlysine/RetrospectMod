package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import theRetrospect.effects.TimelineCircleEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;
import theRetrospect.util.CardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TimeLoopAction extends AbstractGameAction {
    private final TimelineMinion timeline;
    private final int copyNumber;

    public TimeLoopAction(TimelineMinion timeline, int copyNumber) {
        this.timeline = timeline;
        this.copyNumber = copyNumber;
    }

    @Override
    public void update() {
        AbstractDungeon.effectsQueue.add(new TimelineCircleEffect(timeline));
        AbstractDungeon.effectsQueue.add(new EmpowerEffect(timeline.drawX, timeline.drawY));

        List<AbstractCard> cards = new ArrayList<>(timeline.cards);
        for (int i = 0; i < copyNumber; i++) {
            for (AbstractCard card : cards) {
                timeline.addCard(CardUtils.makeAdvancedCopy(card, true));
            }
        }

        Optional<TimerPower> power = timeline.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        power.ifPresent(TimerPower::refresh);

        this.isDone = true;
    }
}
