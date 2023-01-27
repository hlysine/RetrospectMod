package theRetrospect.mechanics.timeline;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.BaseTargeting;

import java.util.List;
import java.util.stream.Collectors;

public class TimelineTargeting extends BaseTargeting<TimelineMinion> {
    @SpireEnum
    public static AbstractCard.CardTarget TIMELINE;

    @Override
    protected List<TimelineMinion> getTargets() {
        return MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> !m.isDeadOrEscaped())
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());
    }
}
