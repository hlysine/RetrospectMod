package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.RewindAction;
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
        addToBot(new RewindAction(this, timelineCount, null));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            int costReduction = info.getBaseCost() - info.getUpgradedCost();
            if (this.cost < info.getBaseCost()) {
                upgradeBaseCost(this.cost - costReduction);
                if (this.cost < 0) {
                    this.cost = 0;
                }
            } else {
                upgradeBaseCost(info.getBaseCost() - costReduction);
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