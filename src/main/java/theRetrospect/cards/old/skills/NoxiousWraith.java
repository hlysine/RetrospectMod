package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class NoxiousWraith extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(NoxiousWraith.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public NoxiousWraith() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int intangible = info.getBaseValue("intangible");
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, intangible), intangible));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber));
    }
}
