package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.TimeLoopAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

public class TimeLoop extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(TimeLoop.class.getSimpleName());

    public TimeLoop() {
        super(ID);
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new TimeLoopAction(target, magicNumber));
    }
}
