package theRetrospect.util;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import basemod.animations.G3DJAnimation;
import basemod.animations.SpineAnimation;
import basemod.animations.SpriterAnimation;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.rits.cloning.Cloner;
import com.rits.cloning.ICloningStrategy;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.patches.AnimationPatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CloneUtils {
    private static final Logger logger = LogManager.getLogger(CloneUtils.class.getName());

    private static final Cloner cloner = new Cloner();
    private static final List<Class<?>> classesToIgnore = Arrays.asList(
//            Texture.class,
//            TextureAtlas.AtlasRegion.class,
//            TextureAtlas.class,
            AbstractPlayer.class,
            AbstractDungeon.class,
            AbstractRoom.class,
            CardCrawlGame.class,
            AbstractRelic.class
    );

    static {
        cloner.registerCloningStrategy((o, field) -> {
            if (classesToIgnore.stream().anyMatch(c -> c.isAssignableFrom(field.getType()))) {
                logger.info("Ignoring " + field.getType().getName() + " in field " + field.getName() + " of class " + o.getClass().getName());
                return ICloningStrategy.Strategy.SAME_INSTANCE_INSTEAD_OF_CLONE;
            }
            return ICloningStrategy.Strategy.IGNORE;
        });
        cloner.registerFastCloner(Texture.class, (o, cloner, clones) -> {
            Texture t = (Texture) o;
            if (t == null) return null;
            if (t.getTextureData() == null) return new Texture(t.getWidth(), t.getHeight(), null);
            return new Texture(t.getTextureData());
        });
        cloner.registerFastCloner(TextureAtlas.AtlasRegion.class, (o, cloner, clones) -> {
            TextureAtlas.AtlasRegion r = (TextureAtlas.AtlasRegion) o;
            if (r == null) return null;
            return new TextureAtlas.AtlasRegion(r);
        });
        cloner.registerFastCloner(Skeleton.class, (o, cloner, clones) -> {
            Skeleton s = (Skeleton) o;
            if (s == null) return null;
            Skeleton cloned = new Skeleton(s);
            if (clones != null)
                clones.put(s.getData(), cloned.getData());
            return cloned;
        });
        cloner.registerFastCloner(Hitbox.class, (o, cloner, clones) -> {
            Hitbox h = (Hitbox) o;
            if (h == null) return null;
            Hitbox hb = new Hitbox(h.x, h.y, h.width, h.height);
            hb.hovered = h.hovered;
            hb.justHovered = h.justHovered;
            hb.clickStarted = h.clickStarted;
            hb.clicked = h.clicked;
            return hb;
        });
        cloner.registerFastCloner(Color.class, (o, cloner, clones) -> {
            Color c = (Color) o;
            if (c == null) return null;
            return c.cpy();
        });
        cloner.setDumpClonedClasses(Settings.isDebug);
    }

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
            } else {
                return new NoAnimation();
            }
        } else {
            return new SpineAnimation(
                    AnimationPatch.CreatureAnimationFieldsPatch.atlasUrl.get(player),
                    AnimationPatch.CreatureAnimationFieldsPatch.skeletonUrl.get(player),
                    AnimationPatch.CreatureAnimationFieldsPatch.animationLoadScale.get(player) / scale
            );
        }
    }

    public static void cloneAnimationStates(AbstractPlayer from, AbstractFriendlyMonster to) {
        if (to.animation instanceof SpineAnimation) {
            AnimationState.TrackEntry current = from.state.getCurrent(0);
            AnimationState.TrackEntry e = to.state.setAnimation(0, current.getAnimation().getName(), current.getLoop());
            AnimationStateData stateData = ReflectionHacks.getPrivate(from, AbstractCreature.class, "stateData");
            to.getStateData().setDefaultMix(stateData.getDefaultMix());
            e.setTimeScale(from.state.getTimeScale());
        }
    }

    public static AbstractPower clonePower(AbstractPower power) {
        if (power instanceof CloneablePowerInterface) {
            return ((CloneablePowerInterface) power).makeCopy();
        } else {
            return cloner.deepClone(power);
        }
    }

    public static AbstractOrb cloneOrb(AbstractOrb orb) {
        return cloner.deepClone(orb);
    }

    public static AbstractRelic cloneRelic(AbstractRelic relic) {
        return cloner.deepClone(relic);
    }

    public static AbstractStance cloneStance(AbstractStance stance) {
        return cloner.deepClone(stance);
    }

    public static AbstractPotion clonePotion(AbstractPotion potion) {
        return cloner.deepClone(potion);
    }

    public static MonsterGroup cloneMonsterGroup(MonsterGroup group) {
        return cloner.deepClone(group);
    }

    public static ArrayList<AbstractCard> cloneCardList(List<AbstractCard> list) {
        return list.stream().map(c -> CardUtils.makeAdvancedCopy(c, true)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static CardGroup cloneCardGroup(CardGroup group) {
        CardGroup newGroup = new CardGroup(group.type);
        newGroup.group = cloneCardList(group.group);
        return newGroup;
    }
}
