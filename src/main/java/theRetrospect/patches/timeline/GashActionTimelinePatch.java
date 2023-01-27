package theRetrospect.patches.timeline;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.defect.GashAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.minions.TimelineMinion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Patch Gash(Claw) card so that it affects timeline cards as well
 */
@SpirePatch(
        clz = GashAction.class,
        method = "update"
)
public class GashActionTimelinePatch {
    public static void Prefix(GashAction __instance) {
        List<TimelineMinion> timelines = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());
        for (TimelineMinion timeline : timelines) {
            for (AbstractCard c : timeline.cards) {
                if (c instanceof Claw) {
                    c.baseDamage += __instance.amount;
                    c.applyPowers();
                }
            }
        }
    }
}
