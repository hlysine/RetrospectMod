package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.DejaVuAction;
import theRetrospect.cards.AbstractBaseCard;

public class DejaVu extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(DejaVu.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public DejaVu() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DejaVuAction(this));
        addToBot(new GainBlockAction(p, p, this.block));
    }

    public void applyPowers() {
        this.baseBlock = this.misc;
        this.block = this.baseBlock;
        super.applyPowers();
        initializeDescription();
    }
}
