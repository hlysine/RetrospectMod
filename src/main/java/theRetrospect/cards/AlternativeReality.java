package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;

import static theRetrospect.RetrospectMod.makeCardPath;

public class AlternativeReality extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(AlternativeReality.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 7;
    private static final int UPGRADE_BLOCK = 3;
    private static final int BASE_CARD_DRAW = 2;
    private static final int UPGRADE_CARD_DRAW = 1;

    public AlternativeReality() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        baseBlock = BASE_BLOCK;
        block = baseBlock;
        baseMagicNumber = BASE_CARD_DRAW;
        magicNumber = baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.drawPile.size() % 2 == 0) {
            addToBot(new DrawCardAction(magicNumber));
        } else {
            addToBot(new GainBlockAction(p, p, block));
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
