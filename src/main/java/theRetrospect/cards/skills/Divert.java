package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.RewindTimeAction;
import theRetrospect.cards.TimeTravelCard;
import theRetrospect.powers.VolatileEnergyPower;

public class Divert extends TimeTravelCard {

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());

    public Divert() {
        super(ID);
    }

    @Override
    protected int getTravelDistance() {
        return timelineCount;
    }

    @Override
    protected void useOnTarget(AbstractPlayer p, AbstractMonster m, AbstractCreature target) {
        addToBot(new RewindTimeAction(this, timelineCount, target == p ? null : (AbstractMonster) target));
        addToBot(new ApplyPowerAction(p, p, new VolatileEnergyPower(p, magicNumber)));
    }
}