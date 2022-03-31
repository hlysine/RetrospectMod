package theRetrospect.util;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface EndOfTurnCardPlaying {
    /**
     * Use this trigger instead of {@link AbstractPower#atEndOfTurn(boolean)} if you wish to
     * queue cards with this power.
     */
    void endOfTurnPlayCards();
}
