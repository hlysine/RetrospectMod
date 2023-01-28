package theRetrospect.patches.timetravel;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.rits.cloning.Cloner;
import com.rits.cloning.IDeepCloner;
import com.rits.cloning.IFastCloner;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.Map;

public class FastClonerPatch {

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
        public static void Insert(Cloner __instance, Object o, Map<Object, Object> clones, Map<Class<?>, IFastCloner> ___fastCloners, Map<Class<?>, IDeepCloner> ___cloners, Class<?> aClass, @ByRef IDeepCloner[] cloner) {
            for (Map.Entry<Class<?>, IFastCloner> entry : ___fastCloners.entrySet()) {
                if (entry.getKey().isAssignableFrom(aClass) && !entry.getKey().equals(aClass)) {
                    Object original = o;
                    cloner[0] = new FastClonerCloner(entry.getValue(), new IDeepCloner() {
                        @Override
                        public <T> T deepClone(T o, Map<Object, Object> clones) {
                            if (o == null) return null;
                            if (o == this) return null;

                            // Prevent cycles, expensive but necessary
                            if (clones != null) {
                                @SuppressWarnings("unchecked") T clone = (T) clones.get(o);
                                if (clone != null) {
                                    return clone;
                                }
                            }

                            Class<?> aClass = o.getClass();
                            IDeepCloner cloner;
                            // the cloner cache is not used because fast cloners that uses this cloner wrapper
                            // (e.g. AbstractMonster) may call clone on itself. Using the cache would cause a stack overflow.
                            cloner = ReflectionHacks.privateMethod(Cloner.class, "findDeepCloner", Class.class).invoke(__instance, aClass);
                            if (cloner == ReflectionHacks.getPrivateStatic(Cloner.class, "IGNORE_CLONER")) {
                                return o;
                            } else if (cloner == ReflectionHacks.getPrivateStatic(Cloner.class, "NULL_CLONER")) {
                                return null;
                            }
                            return cloner.deepClone(o, clones);
                        }
                    });
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
