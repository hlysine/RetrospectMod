package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.IgnoreCapacityChannelAction;
import theRetrospect.characters.TheRetrospect;
import theRetrospect.orbs.TimelineOrb;

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
        this.addToBot(new LoseHPAction(p, p, 15));
        this.addToBot(new IgnoreCapacityChannelAction(
                new TimelineOrb(
                        AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                                .filter(c -> !(c instanceof Divert)).collect(Collectors.toList())))); // todo: use action to access cards played
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