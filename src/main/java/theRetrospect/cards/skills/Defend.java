package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.VolatileEnergyPower;

public class Defend extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Defend.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Defend() {
        super(ID, TARGET);

        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new VolatileEnergyPower(p, block))); // todo: debug only
    }
}
