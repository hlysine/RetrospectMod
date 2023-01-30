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
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.rits.cloning.Cloner;
import com.rits.cloning.ICloningStrategy;
import com.rits.cloning.IDeepCloner;
import com.rits.cloning.IFastCloner;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.mechanics.timetravel.CombatState;
import theRetrospect.patches.AnimationPatch;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
        initializeCloner();
    }

    private static void initializeCloner() {
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

    private static void registerPriorityCloningStrategy(ICloningStrategy strategy) {
        List<ICloningStrategy> cloningStrategies = ReflectionHacks.getPrivate(cloner, Cloner.class, "cloningStrategies");
        if (cloningStrategies == null) {
            cloningStrategies = new ArrayList<>();
            ReflectionHacks.setPrivate(cloner, Cloner.class, "cloningStrategies", cloningStrategies);
        }
        cloningStrategies.add(0, strategy);
    }

    private static void unregisterCloningStrategy(ICloningStrategy strategy) {
        List<ICloningStrategy> cloningStrategies = ReflectionHacks.getPrivate(cloner, Cloner.class, "cloningStrategies");
        cloningStrategies.remove(strategy);
    }

    private static final List<MonsterField> baseMonsterFields = enumerateFieldsForMonster(AbstractMonster.class, Object.class);

    private static final Map<Class<?>, List<MonsterField>> monsterFields = new HashMap<>();

    private static final List<String> alwaysCopyFields = Arrays.asList(
            "damage",
            "powers"
    );
    private static final List<String> alwaysIgnoreFields = Arrays.asList(
            "initialSpawn" // for The Collector
    );

    private static List<MonsterField> enumerateFieldsForMonster(Class<?> monsterClass, Class<?> baseClass) {
        List<MonsterField> fields = new ArrayList<>();
        Class<?> sc = monsterClass;
        do {
            Field[] fs = sc.getDeclaredFields();
            for (final Field f : fs) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                int modifiers = f.getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    fields.add(new MonsterField(f));
                }
            }
        } while (!Objects.equals(sc = sc.getSuperclass(), baseClass) && sc != null);
        return fields;
    }

    /**
     * Fix the incorrect states after a monster is being added to a combat state that it didn't originally belong to.
     * Both the monster and the combat state may be mutated.
     *
     * @param monster     The monster to fix.
     * @param combatState The combat state that the monster has been added to.
     */
    public static void fixMonsterStates(AbstractMonster monster, CombatState combatState) {
        if (monster instanceof AwakenedOne) {
            combatState.cannotLose = ReflectionHacks.getPrivate(monster, AwakenedOne.class, "form1");
        } else if (monster instanceof Darkling) {
            boolean allDead = true;
            for (AbstractMonster m : combatState.monsters.monsters) {
                if (m.id.equals(Darkling.ID) && !m.halfDead) {
                    allDead = false;
                    break;
                }
            }
            combatState.cannotLose = !allDead;
        }
    }

    /**
     * Copies all appropriate creature states from one monster to another, mutating the destination monster.
     * {@link CloneUtils#fixMonsterStates(AbstractMonster, CombatState)} should be called after this method.
     *
     * @param source           The monster to copy from.
     * @param destination      The monster to copy to.
     * @param destinationGroup The monster group that the destination monster belongs to.
     */
    public static void copyMonsterStates(AbstractMonster source, AbstractMonster destination, MonsterGroup destinationGroup) {
        if (!source.getClass().equals(destination.getClass())) {
            throw new IllegalArgumentException("Source and destination monster classes must be the same.");
        }

        logger.info("Starting to copy monster states");
        long startTime = System.currentTimeMillis();

        List<MonsterField> fields = monsterFields.get(source.getClass());
        if (fields == null) {
            fields = enumerateFieldsForMonster(source.getClass(), AbstractMonster.class);
            monsterFields.put(source.getClass(), new ArrayList<>(fields));
        }
        fields.addAll(baseMonsterFields);
        AtomicBoolean containsMonsterField = new AtomicBoolean(false);
        ICloningStrategy strategy = (o, field) -> {
            try {
                if (o instanceof AbstractMonster && o != source) {
                    containsMonsterField.set(true);
                    return ICloningStrategy.Strategy.SAME_INSTANCE_INSTEAD_OF_CLONE;
                } else if (field.get(o) instanceof AbstractMonster) {
                    if (field.get(o) != source) {
                        containsMonsterField.set(true);
                    }
                    return ICloningStrategy.Strategy.SAME_INSTANCE_INSTEAD_OF_CLONE;
                } else if (field.getType().isArray() && field.get(o) instanceof Object[] && Arrays.stream((Object[]) field.get(o)).anyMatch(e -> e instanceof AbstractMonster)) {
                    if (Arrays.stream((Object[]) field.get(o)).anyMatch(e -> e instanceof AbstractMonster && e != source)) {
                        containsMonsterField.set(true);
                    }
                    return ICloningStrategy.Strategy.SAME_INSTANCE_INSTEAD_OF_CLONE;
                } else {
                    return ICloningStrategy.Strategy.IGNORE;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
        IFastCloner fastCloner = (o, cloner1, clones1) -> {
            if (o == null) return null;
            AbstractMonster m = (AbstractMonster) o;
            if (MonsterUtils.isSameMonster(m, source)) {
                return destination;
            }
            for (AbstractMonster m1 : destinationGroup.monsters) {
                if (MonsterUtils.isSameMonster(m, m1)) {
                    return m1;
                }
            }
            return null;
        };
        List<String> ignoredFields = new ArrayList<>();
        List<String> copiedFields = new ArrayList<>();
        // use a shared clones map for all fields to maintain cross-references
        Map<Object, Object> clones = new ClonesMap();
        for (MonsterField field : fields) {
            if (field.ignore || alwaysIgnoreFields.contains(field.field.getName())) {
                ignoredFields.add(field.field.getName());
                continue;
            }
            try {
                Object value = field.field.get(source);
                if (!alwaysCopyFields.contains(field.field.getName())) {
                    containsMonsterField.set(false);
                    // expensive but easiest way to check if the field should be cloned at all
                    registerPriorityCloningStrategy(strategy);
                    cloner.deepClone(value);
                    unregisterCloningStrategy(strategy);
                    if (containsMonsterField.get()) {
                        field.ignore = true;
                        ignoredFields.add(field.field.getName());
                        continue;
                    }
                }
                cloner.registerFastCloner(AbstractMonster.class, fastCloner);
                value = ReflectionHacks.privateMethod(Cloner.class, "cloneInternal", Object.class, Map.class).invoke(cloner, value, clones);
                cloner.unregisterFastCloner(AbstractMonster.class);
                field.field.set(destination, value);
                copiedFields.add(field.field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        logger.info("Monster field copy complete in " + (System.currentTimeMillis() - startTime) + "ms: " + source.getClass().getSimpleName() + " -> " + destination.getClass().getSimpleName());
        logger.info("Ignored fields: " + ignoredFields);
        logger.info("Copied fields: " + copiedFields);
    }

    private static class MonsterField {
        Field field;
        boolean ignore = false;

        MonsterField(Field field) {
            this.field = field;
        }
    }

    private static class ClonesMap extends IdentityHashMap<Object, Object> {
        @Override
        public Object get(Object key) {
            Map<Object, Object> ignoredInstances = ReflectionHacks.getPrivate(cloner, Cloner.class, "ignoredInstances");
            if (ignoredInstances != null) {
                Object o = ignoredInstances.get(key);
                if (o != null) return o;
            }
            return super.get(key);
        }
    }
}
