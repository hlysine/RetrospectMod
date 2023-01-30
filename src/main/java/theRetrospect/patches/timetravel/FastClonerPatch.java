package theRetrospect.patches.timetravel;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.rits.cloning.Cloner;
import com.rits.cloning.IDeepCloner;
import com.rits.cloning.IFastCloner;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.Map;

public class FastClonerPatch {
    // This is a patch to allow fast cloners to be associated with a base class type, so that all subclasses can be fast cloned.
    @SpirePatch(
            clz = Cloner.class,
            method = "cloneInternal"
    )
    public static class FindDeepClonerForSubClass {

        private static class FastClonerCloner implements IDeepCloner {
            private final IFastCloner fastCloner;
            private final IDeepCloner cloneInternal;

            FastClonerCloner(IFastCloner fastCloner, IDeepCloner fallbackCloner) {
                this.fastCloner = fastCloner;
                this.cloneInternal = fallbackCloner;
            }

            public <T> T deepClone(T o, Map<Object, Object> clones) {
                @SuppressWarnings("unchecked") T clone = (T) fastCloner.clone(o, cloneInternal, clones);
                if (clones != null) clones.put(o, clone);
                return clone;
            }
        }

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"aClass", "cloner"}
        )
        public static void Insert(Cloner __instance, Object o, Map<Object, Object> clones, IDeepCloner ___deepCloner, Map<Class<?>, IFastCloner> ___fastCloners, Class<?> aClass, @ByRef IDeepCloner[] cloner) {
            for (Map.Entry<Class<?>, IFastCloner> entry : ___fastCloners.entrySet()) {
                if (entry.getKey().isAssignableFrom(aClass) && !entry.getKey().equals(aClass)) {
                    cloner[0] = new FastClonerCloner(entry.getValue(), ___deepCloner);
                }
            }
        }


        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Cloner.class, "findDeepCloner");

                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1};
            }
        }
    }

    // these patches update the internal cloners cache when a fast cloner is registered or unregistered
    @SpirePatch(
            clz = Cloner.class,
            method = "registerFastCloner"
    )
    public static class UpdateClonerCacheOnRegister {
        public static void Postfix(Cloner __instance, Class<?> c, IFastCloner fastCloner, Map<Class<?>, IDeepCloner> ___cloners) {
            ___cloners.entrySet().removeIf(entry -> c.isAssignableFrom(entry.getKey()));
        }
    }

    @SpirePatch(
            clz = Cloner.class,
            method = "unregisterFastCloner"
    )
    public static class UpdateClonerCacheOnUnregister {
        public static void Postfix(Cloner __instance, Class<?> c, Map<Class<?>, IDeepCloner> ___cloners) {
            ___cloners.entrySet().removeIf(entry -> c.isAssignableFrom(entry.getKey()));
        }
    }
}
