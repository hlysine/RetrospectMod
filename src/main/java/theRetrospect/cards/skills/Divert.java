package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;

public class Divert extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Divert() {
        super(ID, TARGET);

        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (!this.upgraded) {
            this.isEthereal = false;
        }
    }
}