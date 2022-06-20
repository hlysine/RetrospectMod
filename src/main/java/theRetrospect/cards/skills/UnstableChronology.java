package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;

public class UnstableChronology extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(UnstableChronology.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public UnstableChronology() {
        super(ID, TARGET);
    }

    @Override
    public void tookDamage() {
        updateCost(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (this.cost < info.getBaseCost()) {
                upgradeBaseCost(this.cost - 1);
                if (this.cost < 0) {
                    this.cost = 0;
                }
            } else {
                upgradeBaseCost(info.getBaseCost() - 1);
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard tmp = new UnstableChronology();
        if (AbstractDungeon.player != null) {
            tmp.updateCost(-AbstractDungeon.player.damagedThisCombat);
        }
        return tmp;
    }
}