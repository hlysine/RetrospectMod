package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.FrozenPower;

public class Overcore extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Overcore.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Overcore() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
        addToBot(new ApplyPowerAction(p, p, new FrozenPower(p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.exhaust = false;
        }
        super.upgrade();
    }
}