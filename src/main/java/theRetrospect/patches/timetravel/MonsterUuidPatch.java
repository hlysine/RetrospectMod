package theRetrospect.patches.timetravel;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.UUID;

@SpirePatch(
        clz = AbstractMonster.class,
        method = SpirePatch.CLASS
)
public class MonsterUuidPatch {
    /**
     * A unique identifier for each monster.
     * This is used to identify the same monster across different timelines since the class instances are different.
     * The UUID is automatically copied when the monster is cloned for time travel.
     */
    public static final SpireField<UUID> uuid = new SpireField<>(UUID::randomUUID);
}
