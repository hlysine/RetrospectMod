package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimeLoopPower;

public class TimeLoop extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(TimeLoop.class.getSimpleName());

    public TimeLoop() {
        super(ID);
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new ApplyPowerAction(target, p, new TimeLoopPower(target)));
    }
}
