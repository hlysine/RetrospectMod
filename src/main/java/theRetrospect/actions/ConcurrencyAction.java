package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;

public class ConcurrencyAction extends AbstractGameAction {
    private final DamageInfo info;
    private final AbstractCreature target;

    public ConcurrencyAction(AbstractCreature m, DamageInfo info) {
        this.info = info;
        this.target = m;
    }

    @Override
    public void update() {
        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
        for (AbstractMonster monster : minions.monsters) {
            if (monster instanceof TimelineMinion) {
                addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
            }
        }

        this.isDone = true;
    }
}
