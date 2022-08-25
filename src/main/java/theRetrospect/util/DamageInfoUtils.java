package theRetrospect.util;

import com.megacrit.cardcrawl.cards.DamageInfo;
import theRetrospect.patches.NoVfxDamagePatch;

public class DamageInfoUtils {
    public static boolean getNoVisualEffect(DamageInfo info) {
        return NoVfxDamagePatch.DamageInfoAddFieldPatch.noVisualEffect.get(info);
    }

    public static void setNoVisualEffect(DamageInfo info, boolean value) {
        NoVfxDamagePatch.DamageInfoAddFieldPatch.noVisualEffect.set(info, value);
    }

    public static DamageInfo withNoVisualEffect(DamageInfo info) {
        setNoVisualEffect(info, true);
        return info;
    }
}
