package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.DejaVuAction;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class DejaVu extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(DejaVu.class.getSimpleName());
    public static final String IMG = makeCardPath("deja_vu.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 1;
    private static final int BLOCK_INCREASE = 1;
    private static final int UPGRADE_BLOCK_INCREASE = 1;

    public DejaVu() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        block = baseBlock = misc = BASE_BLOCK;
        magicNumber = baseMagicNumber = BLOCK_INCREASE;
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

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_BLOCK_INCREASE);
            initializeDescription();
        }
    }
}
