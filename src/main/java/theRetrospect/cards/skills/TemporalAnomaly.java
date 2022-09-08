package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.AdvanceTimelineAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.CardUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class TemporalAnomaly extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(TemporalAnomaly.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public TemporalAnomaly() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = CardUtils.calculateXCostEffect(this);

        if (this.upgraded)
            effect += 1;

        if (effect > 0) {
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }

            AtomicInteger remainingEffect = new AtomicInteger(effect);
            CallbackUtils.ForLoop(
                    () -> remainingEffect.get() > 0,
                    remainingEffect::decrementAndGet,
                    next -> addToBot(new AdvanceTimelineAction(null, true, new AbstractGameAction() {
                        @Override
                        public void update() {
                            next.run();
                            this.isDone = true;
                        }
                    }))
            );
        }
    }
}