package theRetrospect.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.SilentHealthChangeAction;
import theRetrospect.actions.general.WaitForDeathAction;
import theRetrospect.actions.timelineActions.RepositionTimelinesAction;
import theRetrospect.actions.timelineActions.TriggerAfterTimelineCollapseAction;
import theRetrospect.effects.TimelineCollapseEffect;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineCollapseSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TimelineUtils {
    public static final String TIMELINE_TIP = RetrospectMod.makeID("TIMELINE_TIP");

    public static final List<TimelineMinion> timelinesConstructedThisCombat = new ArrayList<>();

    public static void repositionTimelines(AbstractPlayer player) {
        boolean isPlayerSurrounded = player.drawX > Settings.WIDTH * 0.4f;

        float midX = Settings.WIDTH * (isPlayerSurrounded ? 0.5f : 0.35f);
        float y = AbstractDungeon.floorY + player.hb_h * (isPlayerSurrounded ? 0.9f : 0.1f);
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
            startX += gap;
            player.movePosition(Math.min(Settings.WIDTH * 0.25f, startX), player.drawY);
            for (int i = 0; i < player.orbs.size(); i++) {
                player.orbs.get(i).setSlot(i, player.maxOrbs);
            }
        }
    }

    public static TimelineMinion getRandomTimeline(AbstractPlayer player) {
        return getRandomTimeline(player, null);
    }

    public static TimelineMinion getRandomTimeline(AbstractPlayer player, Predicate<TimelineMinion> timelineFilter) {
        MonsterGroup monsters = MinionUtils.getMinions(player);
        List<TimelineMinion> timelines = monsters.monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .filter(m -> timelineFilter == null || timelineFilter.test(m))
                .collect(Collectors.toList());
        if (timelines.size() == 0) {
            return null;
        } else if (timelines.size() == 1) {
            return timelines.get(0);
        }
        return timelines.get(AbstractDungeon.cardRandomRng.random(timelines.size() - 1));
    }

    public static List<TimelineMinion> getTimelines(AbstractPlayer player) {
        return MinionUtils.getMinions(player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());
    }

    public static boolean notifyCollapse(AbstractFriendlyMonster minion) {
        boolean canCollapse = true;

        for (AbstractPower power : minion.powers) {
            if (power instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) power;
                canCollapse = listener.beforeTimelineCollapse((TimelineMinion) minion, canCollapse) && canCollapse;
            }
        }

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) power;
                canCollapse = listener.beforeTimelineCollapse((TimelineMinion) minion, canCollapse) && canCollapse;
            }
        }

        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof TimelineCollapseSubscriber) {
                TimelineCollapseSubscriber listener = (TimelineCollapseSubscriber) relic;
                canCollapse = listener.beforeTimelineCollapse((TimelineMinion) minion, canCollapse) && canCollapse;
            }
        }

        return canCollapse;
    }

    public static void queuedCollapse(AbstractFriendlyMonster minion) {
        CardCrawlGame.sound.playA("STANCE_ENTER_CALM", 0.1f);
        AbstractDungeon.actionManager.addToTop(new TriggerAfterTimelineCollapseAction((TimelineMinion) minion));
        AbstractDungeon.actionManager.addToTop(new RepositionTimelinesAction());
        AbstractDungeon.actionManager.addToTop(new WaitForDeathAction(minion));
        AbstractDungeon.actionManager.addToTop(new SilentHealthChangeAction(AbstractDungeon.player, minion.currentHealth));
        AbstractDungeon.effectsQueue.add(new TimelineCollapseEffect(minion));
        minion.die(false);
    }

    public static void instantCollapseWithEffect(AbstractFriendlyMonster minion) {
        CardCrawlGame.sound.playA("STANCE_ENTER_CALM", 0.1f);
        AbstractDungeon.actionManager.addToTop(new TriggerAfterTimelineCollapseAction((TimelineMinion) minion));
        AbstractDungeon.actionManager.addToTop(new RepositionTimelinesAction());
        AbstractDungeon.actionManager.addToTop(new WaitForDeathAction(minion));
        AbstractDungeon.effectsQueue.add(new TimelineCollapseEffect(minion));
        instantCollapseWithoutEffect(minion);
    }

    public static void instantCollapseWithoutEffect(AbstractFriendlyMonster minion) {
        new SilentHealthChangeAction(AbstractDungeon.player, minion.currentHealth).update();
        minion.die(false);
    }
}
