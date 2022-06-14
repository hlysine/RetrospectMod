package theRetrospect.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnDeathPreProtectionSubscriber {
    /**
     * Triggered when the creature (monster or player) is about to die.
     * This is triggered before protections such as Lizard Tail and Fairy In A Bottle take effect.
     *
     * @param info   The {@link DamageInfo} that caused the death.
     * @param canDie If false, other subscribers have already prevented death.
     * @return Whether to allow the creature to die.
     */
    boolean onDeathPreProtection(DamageInfo info, boolean canDie);
}
