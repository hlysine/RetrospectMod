package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.TimeLoopPower;
import theRetrospect.util.TimelineTargeting;
import theRetrospect.util.TimelineUtils;

import static theRetrospect.RetrospectMod.makeCardPath;

public class TimeLoop extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(TimeLoop.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("time_loop.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = TimelineTargeting.TIMELINE;
    private static final CardType TYPE = CardType.POWER;

    private static final int BASE_COST = 3;

    public TimeLoop() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TimelineMinion target = (TimelineMinion) TimelineTargeting.getTarget(this);
        if (target == null) target = TimelineUtils.getRandomTimeline(p);
        if (target == null) return;

        addToBot(new ApplyPowerAction(target, p, new TimeLoopPower(target)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
            initializeDescription();
        }
    }
}
