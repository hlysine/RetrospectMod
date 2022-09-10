package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import theRetrospect.util.TimelineUtils;

public class RepositionTimelinesOnMonsterChangePatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {boolean.class}
    )
    public static class RepositionTimelinesOnMonsterDeathPatch {
        public static void Postfix(AbstractMonster __instance, boolean triggerRelics) {
            TimelineUtils.repositionTimelines(AbstractDungeon.player);
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addMonster",
            paramtypez = {AbstractMonster.class}
    )
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addSpawnedMonster"
    )
    @SpirePatch(
            clz = MonsterGroup.class,
            method = "add"
    )
    public static class RepositionTimelinesOnMonsterSpawnPatch {
        public static void Postfix(MonsterGroup __instance, AbstractMonster monster) {
            if (!(monster instanceof AbstractFriendlyMonster))
                TimelineUtils.repositionTimelines(AbstractDungeon.player);
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "addMonster",
            paramtypez = {int.class, AbstractMonster.class}
    )
    public static class RepositionTimelinesOnMonsterInsertPatch {
        public static void Postfix(MonsterGroup __instance, int index, AbstractMonster monster) {
            if (!(monster instanceof AbstractFriendlyMonster))
                TimelineUtils.repositionTimelines(AbstractDungeon.player);
        }
    }
}