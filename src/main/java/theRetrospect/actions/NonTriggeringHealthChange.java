package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class NonTriggeringHealthChange extends AbstractGameAction {

    private final AbstractCreature creature;
    private final int change;

    public NonTriggeringHealthChange(AbstractCreature creature, int change) {
        this.creature = creature;
        this.change = change;
    }

    @Override
    public void update() {
        creature.currentHealth = Math.max(0, Math.min(creature.maxHealth, creature.currentHealth + change));
        creature.healthBarUpdatedEvent();
        this.isDone = true;
    }
}
