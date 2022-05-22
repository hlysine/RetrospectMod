package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.CollapseTimelineAction;
import theRetrospect.minions.TimelineMinion;

import static theRetrospect.RetrospectMod.makeCardPath;

public class TacticalRetreat extends TimelineTargetingCard {

    public static final String ID = RetrospectMod.makeID(TacticalRetreat.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("tactical_retreat.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardType TYPE = CardType.SKILL;

    private static final int BASE_BLOCK = 7;
    private static final int UPGRADE_BLOCK = 4;

    private static final int COST = 1;

    public TacticalRetreat() {
        super(ID, IMG, COST, TYPE, RARITY);

        block = baseBlock = BASE_BLOCK;
    }

    @Override
    public void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target) {
        addToBot(new CollapseTimelineAction(target));
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.initializeDescription();
        }
    }
}