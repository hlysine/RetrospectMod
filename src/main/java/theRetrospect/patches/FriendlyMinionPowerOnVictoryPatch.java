package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "onVictory"
)
public class FriendlyMinionPowerOnVictoryPatch {
    public static void Postfix(AbstractPlayer __instance) {
        if (!__instance.isDying) {
            MonsterGroup minions;
            if (__instance instanceof AbstractPlayerWithMinions) {
                AbstractPlayerWithMinions player = (AbstractPlayerWithMinions) __instance;
                minions = player.minions;
            } else {
                minions = BasePlayerMinionHelper.getMinions(__instance);
            }
            ArrayList<AbstractMonster> monsters = minions.monsters;
            for (int i = monsters.size() - 1; i >= 0; i--) {
                AbstractMonster monster = monsters.get(i);
                for (AbstractPower power : monster.powers) {
                    power.onVictory();
                }
            }
        }
    }
}