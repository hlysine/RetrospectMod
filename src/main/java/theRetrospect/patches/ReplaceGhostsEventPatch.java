package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import theRetrospect.characters.TheRetrospect;
import theRetrospect.events.Ghosts;

@SpirePatch(
        clz = EventHelper.class,
        method = "getEvent"
)
public class ReplaceGhostsEventPatch {
    public static AbstractEvent Postfix(AbstractEvent __result, String __key) {
        if ("Ghosts".equals(__key)) {
            if (AbstractDungeon.player.chosenClass == TheRetrospect.Enums.THE_RETROSPECT) {
                return new Ghosts();
            }
        }
        return __result;
    }
}
