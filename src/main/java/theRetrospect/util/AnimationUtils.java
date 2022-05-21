package theRetrospect.util;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import basemod.animations.G3DJAnimation;
import basemod.animations.SpineAnimation;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import theRetrospect.minions.AbstractAnimatedFriendlyMonster;
import theRetrospect.patches.AnimationPatch;

public class AnimationUtils {

    public static AbstractAnimation cloneAnimation(AbstractPlayer player, float scale) {
        if (player instanceof CustomPlayer) {
            CustomPlayer customPlayer = (CustomPlayer) player;
            AbstractAnimation animation = ReflectionHacks.getPrivate(customPlayer, CustomPlayer.class, "animation");
            if (animation instanceof SpineAnimation) {
                SpineAnimation anim = (SpineAnimation) animation;
                return new SpineAnimation(anim.atlasUrl, anim.skeletonUrl, anim.scale / scale);
            } else if (animation instanceof SpriterAnimation) {
                SpriterAnimation anim = (SpriterAnimation) animation;
                SpriterAnimation newAnim = new SpriterAnimation(AnimationPatch.SpriterAnimationFieldsPatch.filePath.get(anim));
                newAnim.myPlayer.setScale(scale);
                return newAnim;
            } else if (animation instanceof G3DJAnimation) {
                G3DJAnimation anim = (G3DJAnimation) animation;
                G3DJAnimation newAnim = new G3DJAnimation(
                        ReflectionHacks.getPrivate(anim, G3DJAnimation.class, "modelString"),
                        ReflectionHacks.getPrivate(anim, G3DJAnimation.class, "animationString")
                );
                ModelInstance animInstance = ReflectionHacks.getPrivate(newAnim, G3DJAnimation.class, "myInstance");
                animInstance.transform.scale(scale, 1.0f, scale);
                return newAnim;
            }
        } else {
            return new SpineAnimation(
                    AnimationPatch.CreatureAnimationFieldsPatch.atlasUrl.get(player),
                    AnimationPatch.CreatureAnimationFieldsPatch.skeletonUrl.get(player),
                    AnimationPatch.CreatureAnimationFieldsPatch.animationLoadScale.get(player) / scale
            );
        }

        return null;
    }

    public static void cloneAnimationStates(AbstractPlayer from, AbstractAnimatedFriendlyMonster to) {
        if (to.animation.type() == AbstractAnimation.Type.NONE) {
            AnimationState.TrackEntry current = from.state.getCurrent(0);
            AnimationState.TrackEntry e = to.state.setAnimation(0, current.getAnimation().getName(), current.getLoop());
            AnimationStateData stateData = ReflectionHacks.getPrivate(from, AbstractCreature.class, "stateData");
            to.getStateData().setDefaultMix(stateData.getDefaultMix());
            e.setTimeScale(from.state.getTimeScale());
        }
    }
}
