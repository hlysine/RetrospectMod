package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.util.CardPlaySource;

import static theRetrospect.RetrospectMod.makeCardPath;

public class RecursiveAffinity extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(RecursiveAffinity.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("recursive_affinity.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_COST = 3;

    public RecursiveAffinity() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ReplayLastCardsAction(card -> card != this, 2, CardPlaySource.CARD));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
            this.initializeDescription();
        }
    }
}