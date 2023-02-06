package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.card.UnceasingEndTurnAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.mechanics.card.CardPlaySource;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimerPower;
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
            for (int i = 0; i < this.magicNumber; i++) {
                TimelineMinion timeline = TimelineUtils.getRandomTimeline(p, t -> t.cards.size() > 0);
                if (timeline != null) {
                    addToTop(new ApplyPowerAction(timeline, p, new TimerPower(timeline)));
                }
            }
        }
        addToBot(new UnceasingEndTurnAction());
    }
}