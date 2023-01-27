package theRetrospect.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.patches.timetravel.MonsterUuidPatch;

import java.util.UUID;

public class MonsterUtils {
    public static UUID getUuid(AbstractMonster monster) {
        return MonsterUuidPatch.uuid.get(monster);
    }

    public static boolean isSameMonster(AbstractMonster monster1, AbstractMonster monster2) {
        return getUuid(monster1).equals(getUuid(monster2));
    }
}
