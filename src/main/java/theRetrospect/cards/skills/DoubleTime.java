package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.AdvanceTimelineAction;
import theRetrospect.actions.timelineActions.CollapseTimelineAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

public class DoubleTime extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(DoubleTime.class.getSimpleName());

    public DoubleTime() {
        super(ID);
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new AdvanceTimelineAction(target, magicNumber, new CollapseTimelineAction(target)));
    }
}