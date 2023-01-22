package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import theRetrospect.util.TimelineUtils;

@SpirePatch(
        clz = GameActionManager.class,
        method = "clear"
)
public class TrackTimelineConstructedPatch {
    public static void Prefix() {
        TimelineUtils.timelinesConstructedThisCombat.clear();
    }
}
