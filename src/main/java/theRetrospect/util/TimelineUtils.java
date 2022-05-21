package theRetrospect.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.minions.TimelineMinion;

import java.util.List;
import java.util.stream.Collectors;

public class TimelineUtils {
    public static void repositionTimelines(AbstractPlayer player) {
        boolean isPlayerSurrounded = player.drawX > Settings.WIDTH * 0.4f;

        float midX = Settings.WIDTH * (isPlayerSurrounded ? 0.5f : 0.35f);
        float y = AbstractDungeon.floorY + player.hb_h * (isPlayerSurrounded ? 1f : 0.2f);
        float gap = 20 * Settings.scale;

        List<AbstractMinionWithCards> minions = MinionUtils.getMinions(player).monsters.stream()
                .filter(m -> m instanceof AbstractMinionWithCards)
                .map(m -> (AbstractMinionWithCards) m)
                .collect(Collectors.toList());

        float width = 0;
        for (AbstractMinionWithCards minion : minions) {
            width += minion.hb_w + gap;
        }

        float startX = midX + width / 2;
        for (int i = 0; i < minions.size(); i++) {
            AbstractMinionWithCards minion = minions.get(i);

            minion.target_x = startX;
            minion.target_y = y + player.hb_h * (i % 2 == 0 ? 0.1f : -0.1f);

            startX -= minion.hb_w + gap;
        }

        if (!isPlayerSurrounded) {
            startX -= player.hb_w / 2;
            player.movePosition(Math.min(Settings.WIDTH * 0.25f, startX), player.drawY);
        }
    }

    public static TimelineMinion getRandomTimeline(AbstractPlayer player) {
        MonsterGroup monsters = MinionUtils.getMinions(player);
        List<TimelineMinion> timelines = monsters.monsters.stream().filter(m -> m instanceof TimelineMinion).map(m -> (TimelineMinion) m).collect(Collectors.toList());
        if (timelines.size() <= 0) {
            return null;
        }
        return timelines.get(AbstractDungeon.cardRng.random(timelines.size() - 1));
    }
}
