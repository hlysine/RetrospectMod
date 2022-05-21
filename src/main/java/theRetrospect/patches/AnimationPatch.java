package theRetrospect.patches;

import basemod.animations.SpriterAnimation;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class AnimationPatch {

    @SpirePatch(
            clz = AbstractCreature.class,
            method = SpirePatch.CLASS
    )
    public static class CreatureAnimationFieldsPatch {
        public static SpireField<String> atlasUrl = new SpireField<>(() -> null);

        public static SpireField<String> skeletonUrl = new SpireField<>(() -> null);

        public static SpireField<Float> animationLoadScale = new SpireField<>(() -> 1.0f);
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loadAnimation"
    )
    public static class LoadAnimationDataPatch {
        public static void Prefix(AbstractCreature __instance, String ___atlasUrl, String ___skeletonUrl, float ___scale) {
            CreatureAnimationFieldsPatch.atlasUrl.set(__instance, ___atlasUrl);
            CreatureAnimationFieldsPatch.skeletonUrl.set(__instance, ___skeletonUrl);
            CreatureAnimationFieldsPatch.animationLoadScale.set(__instance, ___scale);
        }
    }

    @SpirePatch(
            clz = SpriterAnimation.class,
            method = SpirePatch.CLASS
    )
    public static class SpriterAnimationFieldsPatch {
        public static SpireField<String> filePath = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz = SpriterAnimation.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class LoadAnimationPathPatch {
        public static void Prefix(SpriterAnimation __instance, String ___filepath) {
            SpriterAnimationFieldsPatch.filePath.set(__instance, ___filepath);
        }
    }
}
