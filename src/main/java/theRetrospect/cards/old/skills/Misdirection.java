package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.RewindAction;
import theRetrospect.cards.AbstractBaseCard;

public class Misdirection extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Misdirection.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Misdirection() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new RewindAction(this, timelineCount, null));
    }
}