package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FragmentationAction extends AbstractGameAction {
    private final int count;
    private final AbstractCreature owner;
    private final int damage;
    private final DamageInfo.DamageType damageType;

    public FragmentationAction(int count, AbstractCreature owner, int damage, DamageInfo.DamageType damageType) {
        this.count = count;
        this.owner = owner;
        this.damage = damage;
        this.damageType = damageType;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    public void update() {
        if (count > 1)
            addToTop(new FragmentationAction(count - 1, owner, damage, damageType));

        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            addToTop(new DamageAction(this.target, new DamageInfo(owner, damage, damageType), this.attackEffect, true));
        }

        this.isDone = true;
    }
}
