package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.CollapseTimelineAction;
import theRetrospect.actions.TriggerTimelineAction;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;

import static theRetrospect.RetrospectMod.makeCardPath;

public class IntoTheVoid extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("into_the_void.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;

    private static final int COST = 1;

    public IntoTheVoid() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        this.selfRetain = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        MonsterGroup minions = MinionUtils.getMinions(p);
        for (AbstractMonster monster : minions.monsters) {
            if (monster instanceof TimelineMinion) {
                TimelineMinion minion = (TimelineMinion) monster;
                addToBot(new TriggerTimelineAction(minion, false));
                addToBot(new CollapseTimelineAction(minion));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}