package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.CollapseTimelineAction;
import theRetrospect.actions.TriggerTimelineAction;
import theRetrospect.minions.TimelineMinion;

import static theRetrospect.RetrospectMod.makeCardPath;

public class IntoTheVoid extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(IntoTheVoid.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("into_the_void.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_TRIGGER_COUNT = 1;
    private static final int UPGRADE_TRIGGER_COUNT = 1;

    private static final int COST = 1;

    public IntoTheVoid() {
        super(ID, IMG, COST, TYPE, RARITY);

        magicNumber = baseMagicNumber = BASE_TRIGGER_COUNT;
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new TriggerTimelineAction(target, magicNumber, true, new CollapseTimelineAction(target)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_TRIGGER_COUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}