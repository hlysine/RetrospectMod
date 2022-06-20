package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.IntoTheVoidPower;

public class IntoTheVoid extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public IntoTheVoid() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, (int) (p.currentHealth * magicNumber / 100f)));
        addToBot(new ApplyPowerAction(p, p, new IntoTheVoidPower(p, 1)));
    }
}