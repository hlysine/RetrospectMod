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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.monsters.city.TorchHead;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.rits.cloning.*;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.patches.AnimationPatch;

import java.util.*;
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
            CardCrawlGame.class
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

    /**
     * Clones a monster, updating its internal monster references to those in the provided group.
     *
     * @param monster The monster to clone.
     * @param group   The group to update the monster's references to.
     * @return The cloned monster.
     */
    public static AbstractMonster cloneMonster(AbstractMonster monster, MonsterGroup group) {
        cloner.registerFastCloner(AbstractMonster.class, (o, cloner, clones) -> {
            if (o == null) return null;
            AbstractMonster m = (AbstractMonster) o;
            if (MonsterUtils.isSameMonster(m, monster)) {
                IDeepCloner baseCloner = getBaseCloner(o.getClass());
                return baseCloner.deepClone(o, clones);
            }
            for (AbstractMonster m1 : group.monsters) {
                if (MonsterUtils.isSameMonster(m, m1)) {
                    return m1;
                }
            }
            return null;
        });
        cloner.unregisterFastCloner(HashMap.class);
        cloner.registerFastCloner(HashMap.class, (o, cloner, clones) -> {
            if (o == null) return null;
            HashMap<?, ?> map = new HashMap<>((HashMap<?, ?>) o);
            Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
            for (Iterator<? extends Map.Entry<?, ?>> iterator = entries.iterator(); iterator.hasNext(); ) {
                Map.Entry<?, ?> entry = iterator.next();
                if (entry.getValue() instanceof AbstractMonster) {
                    AbstractMonster m = (AbstractMonster) entry.getValue();
                    Optional<AbstractMonster> replacement = group.monsters.stream().filter(m1 -> MonsterUtils.isSameMonster(m, m1)).findFirst();
                    if (!replacement.isPresent())
                        iterator.remove();
                }
                if (entry.getKey() instanceof AbstractMonster) {
                    AbstractMonster m = (AbstractMonster) entry.getKey();
                    Optional<AbstractMonster> replacement = group.monsters.stream().filter(m1 -> MonsterUtils.isSameMonster(m, m1)).findFirst();
                    if (!replacement.isPresent())
                        iterator.remove();
                }
            }

            HashMap<Object, Object> result = new HashMap<>(map.size());
            for (Map.Entry<?, ?> e : map.entrySet()) {
                result.put(cloner.deepClone(e.getKey(), clones), cloner.deepClone(e.getValue(), clones));
            }

            return result;
        });
        cloner.unregisterFastCloner(ArrayList.class);
        cloner.registerFastCloner(ArrayList.class, (o, cloner, clones) -> {
            if (o == null) return null;
            ArrayList<?> list = new ArrayList<>((ArrayList<?>) o);
            Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                Object o1 = it.next();
                if (o1 instanceof AbstractMonster) {
                    AbstractMonster m = (AbstractMonster) o1;
                    Optional<AbstractMonster> replacement = group.monsters.stream().filter(m1 -> MonsterUtils.isSameMonster(m, m1)).findFirst();
                    if (!replacement.isPresent())
                        it.remove();
                }
            }

            ArrayList<Object> result = new ArrayList<>(list.size());
            for (Object e : list) {
                result.add(cloner.deepClone(e, clones));
            }
            return result;
        });
        AbstractMonster newMonster = cloner.deepClone(monster);
        cloner.unregisterFastCloner(AbstractMonster.class);
        cloner.unregisterFastCloner(HashMap.class);
        cloner.registerFastCloner(HashMap.class, new FastClonerHashMap());
        cloner.unregisterFastCloner(ArrayList.class);
        cloner.registerFastCloner(ArrayList.class, new FastClonerArrayList());
        return newMonster;
    }

    /**
     * Clones a monster group, while replacing a specific monster with the provided instance.
     *
     * @param group       The group to clone.
     * @param replacement The monster to replace. This monster will also be cloned.
     * @return The cloned group.
     */
    public static MonsterGroup cloneMonsterGroup(MonsterGroup group, AbstractMonster replacement) {
        AbstractMonster newReplacement = cloneMonster(replacement, group);
        cloner.registerFastCloner(AbstractMonster.class, (o, cloner, clones) -> {
            if (o == null) return null;
            IDeepCloner baseCloner = getBaseCloner(o.getClass());
            if (MonsterUtils.isSameMonster((AbstractMonster) o, newReplacement)) {
                return baseCloner.deepClone(newReplacement, clones);
            } else {
                return baseCloner.deepClone((AbstractMonster) o, clones);
            }
        });
        MonsterGroup newGroup = cloner.deepClone(group);
        newGroup.monsters.replaceAll(m -> fixMonsterState(m, newGroup));
        cloner.unregisterFastCloner(AbstractMonster.class);
        return newGroup;
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

    /**
     * Gets the un-patched cloner for a class.
     * This is to avoid infinite recursion when a fast cloner uses the provided deep cloner to clone itself.
     * Only works for fast cloners that are registered using a base class type.
     *
     * @param clazz The class to get the cloner for.
     * @return The cloner for the class.
     */
    private static IDeepCloner getBaseCloner(Class<?> clazz) {
        return ReflectionHacks.privateMethod(Cloner.class, "findDeepCloner", Class.class).invoke(cloner, clazz);
    }

    /**
     * Fixes the state of a monster after accompanying monsters are changed.
     *
     * @param m     The monster to fix.
     * @param group The monster group the monster is in.
     * @return The fixed monster.
     */
    private static AbstractMonster fixMonsterState(AbstractMonster m, MonsterGroup group) {
        if (m instanceof TheCollector) {
            HashMap<Integer, AbstractMonster> enemySlots = ReflectionHacks.getPrivate(m, TheCollector.class, "enemySlots");

            // If the player kills a Torch Head, then wait until The Collector to summon a new one before rewinding,
            // the new Torch Head will be added to the internal map of The Collector, and then be removed when rewinding
            // because the 2 Torch Heads have different UUIDs. This causes the old Torch Head to not be recognized as a
            // minion of The Collector.
            for (AbstractMonster monster : group.monsters) {
                if (monster instanceof TorchHead) {
                    if (!enemySlots.containsValue(monster)) {
                        for (int i = 1; i < 3; i++) {
                            if (enemySlots.get(i) == null) {
                                enemySlots.put(i, monster);
                                break;
                            }
                        }
                    }
                }
            }

            // The Collector only spawns new minions when one of the minions in its internal map has isDying == true.
            // If The Collector is linked to a state with no minions, it will never spawn new minions without this fix
            // because the internal map is cleared when it is being cloned.
            if (!(boolean) ReflectionHacks.getPrivate(m, TheCollector.class, "initialSpawn")) {
                for (int i = 1; i < 3; i++) {
                    if (enemySlots.get(i) == null) {
                        TorchHead dummy = new TorchHead(0, 0);
                        dummy.isDying = true;
                        dummy.currentHealth = 0;
                        enemySlots.put(i, dummy);
                    }
                }
            }
        }
        return m;
    }
}
