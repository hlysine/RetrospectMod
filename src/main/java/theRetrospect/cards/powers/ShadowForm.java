package theRetrospect.cards.powers;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.ShadowFormPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class ShadowForm extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(ShadowForm.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("shadow_form.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int COST = 3;
    private static final int BASE_CARD_DRAW = 2;
    private static final int UPGRADE_CARD_DRAW = 1;

    public ShadowForm() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_CARD_DRAW;

        this.tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShadowFormPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CARD_DRAW);
            initializeDescription();
        }
    }
}