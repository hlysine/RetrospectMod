package theRetrospect.mechanics.timetravel;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.powers.TimeLinkPower;
import theRetrospect.util.BaseTargeting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTravelTargeting extends BaseTargeting<AbstractCreature> {
    @SpireEnum
    public static AbstractCard.CardTarget TIME_TRAVEL;

    @Override
    protected List<AbstractCreature> getTargets() {
        List<AbstractCreature> targets = AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                .filter(m -> !m.isDeadOrEscaped())
                .filter(m -> m.hasPower(TimeLinkPower.POWER_ID))
                .collect(Collectors.toCollection(ArrayList::new));
        targets.add(0, AbstractDungeon.player);
        return targets;
    }
}
