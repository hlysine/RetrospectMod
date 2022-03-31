package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import theRetrospect.util.MinionUtils;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "onModifyPower"
)
public class OnModifyPowerForTimelinePatch {
    public static void Postfix() {
        MinionUtils.getMinions(AbstractDungeon.player).monsters.forEach(AbstractMonster::applyPowers);
    }
}