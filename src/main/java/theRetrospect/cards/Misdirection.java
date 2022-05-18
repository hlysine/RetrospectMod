package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ConstructTimelineAction;
import theRetrospect.util.CardUtils;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Misdirection extends AbstractTimelineCard {

    public static final String ID = RetrospectMod.makeID(Misdirection.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("misdirection.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_COST = 2;

    public Misdirection() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        timelineCount = baseTimelineCount = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.discardPile.size() > 0) {
            AbstractCard card = p.discardPile.getRandomCard(CardType.ATTACK, true);
            if (card != null) {
                p.discardPile.removeCard(card);
                CardUtils.addActionAfterUse(card, () -> addToBot(new ConstructTimelineAction(this)));
                addToBot(new NewQueueCardAction(card, true, true, true));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
            this.initializeDescription();
        }
    }
}