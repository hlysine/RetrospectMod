package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.CollapseTimelineAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

public class TacticalRetreat extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(TacticalRetreat.class.getSimpleName());

    public TacticalRetreat() {
        super(ID);
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new CollapseTimelineAction(target));
        addToBot(new GainBlockAction(p, block));
    }
}