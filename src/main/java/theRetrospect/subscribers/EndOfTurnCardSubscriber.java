package theRetrospect.subscribers;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface EndOfTurnCardSubscriber {
    /**
     * Use this trigger instead of {@link AbstractPower#atEndOfTurn(boolean)} if you wish to
     * queue cards with this power.
     * Note that you MUST call next when the trigger is complete.
     */
    void triggerOnEndOfTurnForPlayingCard(Runnable next);
}
