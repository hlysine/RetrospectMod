package theRetrospect.subscribers;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface CreatureBlockedDamageSubscriber {
    /**
     * Triggered when the creature blocked damage.
     *
     * @param damageInfo    Info of the damage blocked.
     * @param damageAmount  The amount of damage.
     * @param amountBlocked The amount of block lost because of the damage.
     */
    void onBlockedDamage(DamageInfo damageInfo, int damageAmount, int amountBlocked);
}
