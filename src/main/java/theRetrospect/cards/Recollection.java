package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ChooseFromDiscardPileAction;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Recollection extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Recollection.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("recollection.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 2;
    private static final int BASE_CARD_COUNT = 2;
    private static final int UPGRADE_CARD_COUNT = 1;

    public Recollection() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        this.exhaust = true;
        magicNumber = baseMagicNumber = BASE_CARD_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String menuText;
        if (magicNumber > 1) {
            menuText = cardStrings.EXTENDED_DESCRIPTION[1] + magicNumber + cardStrings.EXTENDED_DESCRIPTION[2];
        } else {
            menuText = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        addToBot(new ChooseFromDiscardPileAction(menuText, magicNumber, false, card -> {
            AbstractCard newCard = card.makeStatEquivalentCopy();
            newCard.setCostForTurn(0);
            addToBot(new MakeTempCardInHandAction(newCard));
        }));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARD_COUNT);
            this.initializeDescription();
        }
    }
}