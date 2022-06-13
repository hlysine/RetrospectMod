package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

import static theRetrospect.RetrospectMod.makeCardPath;

public class AlternativeReality extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(AlternativeReality.class.getSimpleName());
    public static final String IMG = makeCardPath("alternative_reality.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int BASE_CARD_DRAW = 2;
    private static final int UPGRADE_CARD_DRAW = 1;

    public AlternativeReality() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = BASE_CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardUtils.getPlaySource(this) == CardPlaySource.TIMELINE) {
            addToBot(new GainBlockAction(p, p, block));
        } else {
            addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_CARD_DRAW);
            initializeDescription();
        }
    }
}
