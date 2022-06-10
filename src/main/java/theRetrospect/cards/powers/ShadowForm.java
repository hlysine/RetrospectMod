package theRetrospect.cards.powers;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.ShadowFormPower;
import theRetrospect.powers.ShortcutPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class ShadowForm extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(ShadowForm.class.getSimpleName());
    public static final String IMG = makeCardPath("shadow_form.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 3;
    private static final int TIMELINE_HP_REDUCTION = 10;
    private static final int POWER_AMOUNT = 1;

    public ShadowForm() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = TIMELINE_HP_REDUCTION;
        this.isEthereal = true;

        this.tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShortcutPower(p, magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new ShadowFormPower(p, POWER_AMOUNT)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isEthereal = false;
            initializeDescription();
        }
    }
}
