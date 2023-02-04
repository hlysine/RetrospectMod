package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.ShiftTimeAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

public class ReturnTicket extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(ReturnTicket.class.getSimpleName());

    public ReturnTicket() {
        super(ID);

        this.exhaust = true;
    }

    @Override
    protected void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new ShiftTimeAction(this, target, null));
    }
}