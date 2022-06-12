package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineUtils;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Fix GetAllInBattleInstances so that it affects timeline cards as well
 */
@SpirePatch(
        clz = com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.class,
        method = "get"
)
public class GetAllInBattleInstancesPatch {
    public static HashSet<AbstractCard> Postfix(HashSet<AbstractCard> cards, UUID uuid) {
        List<TimelineMinion> timelines = TimelineUtils.getTimelines(AbstractDungeon.player);
        for (TimelineMinion timeline : timelines) {
            for (AbstractCard c : timeline.cards) {
                if (c.uuid.equals(uuid))
                    cards.add(c);
            }
        }
        return cards;
    }
}
