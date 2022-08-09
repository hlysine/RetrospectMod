package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TimelineUtils;

public class UnceasingSprint extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(UnceasingSprint.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public UnceasingSprint() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardUtils.getPlaySource(this) == CardPlaySource.TIMELINE) {
            TimelineMinion timeline = TimelineUtils.getRandomTimeline(p);
            if (timeline != null) {
                addToTop(new ApplyPowerAction(timeline, p, new TimerPower(timeline, 1)));
            }
        }
        addToBot(new PressEndTurnButtonAction());
    }
}
