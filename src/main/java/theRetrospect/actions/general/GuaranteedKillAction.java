package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class GuaranteedKillAction extends AbstractGameAction {

    private final AbstractCreature creature;

    public GuaranteedKillAction(AbstractCreature creature) {
        this.creature = creature;
    }

    @Override
    public void update() {
        creature.currentHealth = 0;
        creature.healthBarUpdatedEvent();
        for (int i = 0; i < 99999 && !creature.isDeadOrEscaped(); i++) {
            creature.damage(new DamageInfo(null, 99999, DamageInfo.DamageType.HP_LOSS));
        }
        this.isDone = true;
    }
}
