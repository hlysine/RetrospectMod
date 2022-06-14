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

public class OnDeathPreProtectionPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class OnPlayerDeathPreProtectionPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
            boolean canDie = true;
            for (AbstractPower power : __instance.powers) {
                if (power instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) power;
                    canDie = subscriber.onDeathPreProtection(info, canDie) && canDie;
                }
            }
            for (AbstractRelic relic : __instance.relics) {
                if (relic instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) relic;
                    canDie = subscriber.onDeathPreProtection(info, canDie) && canDie;
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

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(AbstractMonster __instance, DamageInfo info) {
            boolean canDie = true;
            for (AbstractPower power : __instance.powers) {
                if (power instanceof OnDeathPreProtectionSubscriber) {
                    OnDeathPreProtectionSubscriber subscriber = (OnDeathPreProtectionSubscriber) power;
                    canDie = subscriber.onDeathPreProtection(info, canDie) && canDie;
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
