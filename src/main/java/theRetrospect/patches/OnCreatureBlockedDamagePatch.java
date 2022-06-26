package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theRetrospect.subscribers.CreatureBlockedDamageSubscriber;

import java.util.HashMap;
import java.util.Map;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "decrementBlock"
)
public class OnCreatureBlockedDamagePatch {
    private static final Map<AbstractCreature, Integer> blockBeforeDamage = new HashMap<>();

    public static void Prefix(AbstractCreature __instance) {
        blockBeforeDamage.put(__instance, __instance.currentBlock);
    }

    public static void Postfix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
        int blockLost = blockBeforeDamage.get(__instance) - __instance.currentBlock;

        for (AbstractPower power : __instance.powers) {
            if (power instanceof CreatureBlockedDamageSubscriber) {
                CreatureBlockedDamageSubscriber subscriber = (CreatureBlockedDamageSubscriber) power;
                subscriber.onBlockedDamage(info, damageAmount, blockLost);
            }
        }
        if (__instance instanceof AbstractPlayer) {
            AbstractPlayer player = (AbstractPlayer) __instance;
            for (AbstractRelic relic : player.relics) {
                if (relic instanceof CreatureBlockedDamageSubscriber) {
                    CreatureBlockedDamageSubscriber subscriber = (CreatureBlockedDamageSubscriber) relic;
                    subscriber.onBlockedDamage(info, damageAmount, blockLost);
                }
            }
        }
    }
}

