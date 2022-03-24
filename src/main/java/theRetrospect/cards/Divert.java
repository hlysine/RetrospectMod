package theRetrospect.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.characters.TheRetrospect;
import theRetrospect.minions.TimelineMinion;

import java.util.List;
import java.util.stream.Collectors;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Divert extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = RetrospectMod.makeID(Divert.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheRetrospect.Enums.COLOR_GRAY;

    private static final int COST = 1;

    // /STAT DECLARATION/

    public Divert() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions player = (AbstractPlayerWithMinions) p;
            AbstractFriendlyMonster minion = new TimelineMinion(
                    filterReplayableCards(AbstractDungeon.actionManager.cardsPlayedThisTurn),
                    -700, 0);
            player.addMinion(minion);
        }
    }

    private List<AbstractCard> filterReplayableCards(List<AbstractCard> cards) {
        // todo: add isReplayable field to base card class
        return cards.stream()
                .filter(c -> !(c instanceof Divert)).map(AbstractCard::makeStatEquivalentCopy).collect(Collectors.toList());
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.initializeDescription();
        }
    }
}