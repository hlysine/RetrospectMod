package theRetrospect.cards.powers;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.ShadowFormPower;

public class ShadowForm extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ShadowForm.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public ShadowForm() {
        super(ID, TARGET);

        this.isEthereal = true;

        this.tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShadowFormPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.isEthereal = false;
        }
        super.upgrade();
    }
}
