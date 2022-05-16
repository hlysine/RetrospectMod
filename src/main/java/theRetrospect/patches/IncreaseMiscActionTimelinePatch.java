package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Fix IncreaseMiscAction so that it affects timeline cards as well
 */
@SpirePatch(
        clz = IncreaseMiscAction.class,
        method = "update"
)
public class IncreaseMiscActionTimelinePatch {
    public static void Prefix(IncreaseMiscAction __instance, UUID ___uuid, int ___miscIncrease) {
        List<TimelineMinion> timelines = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());

        for (TimelineMinion timeline : timelines) {
            for (AbstractCard c : timeline.cards) {
                if (!c.uuid.equals(___uuid))
                    continue;
                c.misc += ___miscIncrease;
                c.applyPowers();
                c.baseBlock = c.misc;
                c.isBlockModified = false;
            }
        }
    }
}
