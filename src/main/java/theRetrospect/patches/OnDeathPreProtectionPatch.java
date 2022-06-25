package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theRetrospect.subscribers.OnDeathPreProtectionSubscriber;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnDeathPreProtectionPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class OnPlayerDeathPreProtectionPatch {
        private static final Map<AbstractPlayer, Integer> healthBeforeDamage = new HashMap<>();

        public static void Prefix(AbstractPlayer __instance) {
            healthBeforeDamage.put(__instance, __instance.currentHealth);
        }

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"damageAmount"}
        )
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
            OnDeathPreProtectionSubscriber.DeathInfo deathInfo = new OnDeathPreProtectionSubscriber.DeathInfo(healthBeforeDamage.get(__instance), damageAmount);
            boolean canDie = true;
            List<AbstractPower> powers = __instance.powers.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (AbstractPower power : powers) {
                if (power instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) power;
                    canDie = subscriber.onDeathPreProtection(info, deathInfo, canDie) && canDie;
                }
            }
            List<AbstractRelic> relics = __instance.relics.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (AbstractRelic relic : relics) {
                if (relic instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) relic;
                    canDie = subscriber.onDeathPreProtection(info, deathInfo, canDie) && canDie;
                }
            }
            if (canDie) {
                return SpireReturn.Continue();
            } else {
                return SpireReturn.Return();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class OnMonsterDeathPreProtectionPatch {
        private static final Map<AbstractMonster, Integer> healthBeforeDamage = new HashMap<>();

        public static void Prefix(AbstractMonster __instance) {
            healthBeforeDamage.put(__instance, __instance.currentHealth);
        }

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"damageAmount"}
        )
        public static SpireReturn<Void> Insert(AbstractMonster __instance, DamageInfo info, int damageAmount) {
            OnDeathPreProtectionSubscriber.DeathInfo deathInfo = new OnDeathPreProtectionSubscriber.DeathInfo(healthBeforeDamage.get(__instance), damageAmount);
            boolean canDie = true;
            for (AbstractPower power : __instance.powers) {
                if (power instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) power;
                    canDie = subscriber.onDeathPreProtection(info, deathInfo, canDie) && canDie;
                }
            }
            if (canDie) {
                return SpireReturn.Continue();
            } else {
                return SpireReturn.Return();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "die");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
