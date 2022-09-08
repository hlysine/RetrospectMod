package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.util.TimelineUtils;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {boolean.class}
)
public class RepositionTimelinesOnMonsterDeathPatch {
    public static void Postfix(AbstractMonster __instance, boolean triggerRelics) {
        TimelineUtils.repositionTimelines(AbstractDungeon.player);
    }
}
