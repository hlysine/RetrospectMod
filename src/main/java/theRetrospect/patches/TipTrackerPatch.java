package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.TipTracker;
import theRetrospect.util.TimelineUtils;

public class TipTrackerPatch {
    @SpirePatch(
            clz = TipTracker.class,
            method = "refresh"
    )
    public static class RefreshPatch {
        public static void Postfix() {
            TipTracker.tips.put(TimelineUtils.TIMELINE_TIP, TipTracker.pref.getBoolean(TimelineUtils.TIMELINE_TIP, false));
        }
    }

    @SpirePatch(
            clz = TipTracker.class,
            method = "disableAllFtues"
    )
    public static class DisableAllFtuesPatch {
        public static void Postfix() {
            TipTracker.neverShowAgain(TimelineUtils.TIMELINE_TIP);
        }
    }
}
