package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.QuickThinkingPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class QuickThinker extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(QuickThinker.class.getSimpleName());
    public static final String IMG = makeCardPath("quick_thinker.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int BASE_COST = 1;
    private static final int CARD_DRAW = 1;

    public QuickThinker() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new QuickThinkingPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
