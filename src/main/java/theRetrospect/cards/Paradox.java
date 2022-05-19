package theRetrospect.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Paradox extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Paradox.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("empty.png");

    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.STATUS;
    private static final int COST = -2;

    public Paradox() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        this.delusional = true;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (AbstractDungeon.player.hand.size() >= 10) {
            card.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
