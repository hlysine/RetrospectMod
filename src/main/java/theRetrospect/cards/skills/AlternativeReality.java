package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class AlternativeReality extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(AlternativeReality.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("alternative_reality.png");


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

        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = BASE_CARD_DRAW;
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

    private int descriptionIdx = 0;

    @Override
    public void update() {
        super.update();
        int lastDescriptionIdx = descriptionIdx;
        if (AbstractDungeon.player == null || AbstractDungeon.player.drawPile == null) {
            this.rawDescription = cardStrings.DESCRIPTION;
            descriptionIdx = 0;
        } else if (AbstractDungeon.player.drawPile.size() % 2 == 0) {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
            descriptionIdx = 1;
        } else {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
            descriptionIdx = 2;
        }
        if (lastDescriptionIdx != descriptionIdx) {
            initializeDescription();
        }
    }
}
