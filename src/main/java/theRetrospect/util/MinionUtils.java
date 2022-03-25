package theRetrospect.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import kobting.friendlyminions.characters.AbstractPlayerWithMinions;
import kobting.friendlyminions.helpers.BasePlayerMinionHelper;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

public class MinionUtils {

    public static MonsterGroup getMinions(AbstractPlayer player) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            return target.minions;
        }

        return BasePlayerMinionHelper.getMinions(player);
    }

    public static void changeMaxMinionAmount(AbstractPlayer player, int newMax) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            target.changeMaxMinionAmount(newMax);
            return;
        }

        BasePlayerMinionHelper.changeMaxMinionAmount(player, newMax);
    }

    public static boolean addMinion(AbstractPlayer player, AbstractFriendlyMonster minionToAdd) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            return target.addMinion(minionToAdd);
        }

        return BasePlayerMinionHelper.addMinion(player, minionToAdd);
    }

    public static boolean removeMinion(AbstractPlayer player, AbstractFriendlyMonster minionToRemove) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            return target.removeMinion(minionToRemove);
        }

        return BasePlayerMinionHelper.removeMinion(player, minionToRemove);
    }


    public static boolean hasMinions(AbstractPlayer player) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            return target.hasMinions();
        }

        return BasePlayerMinionHelper.hasMinions(player);
    }

    public static int getMaxMinions(AbstractPlayer player) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            return target.getMaxMinions();
        }

        return BasePlayerMinionHelper.getMaxMinions(player);
    }

    public static void clearMinions(AbstractPlayer player) {
        if (player instanceof AbstractPlayerWithMinions) {
            AbstractPlayerWithMinions target = (AbstractPlayerWithMinions) player;
            target.clearMinions();
            return;
        }

        BasePlayerMinionHelper.clearMinions(player);
    }

}
