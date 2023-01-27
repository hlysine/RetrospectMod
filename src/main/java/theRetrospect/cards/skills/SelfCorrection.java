package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.SelfCorrectionAction;
import theRetrospect.cards.TimelineTargetingCard;
import theRetrospect.minions.TimelineMinion;

public class SelfCorrection extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(SelfCorrection.class.getSimpleName());

    public SelfCorrection() {
        super(ID);
    }

    @Override
    protected void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new SelfCorrectionAction(target));
    }
}