package theRetrospect.actions.timetravel;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.effects.FlyingOrbEffect;
import theRetrospect.effects.TimeTravelEffect;
import theRetrospect.effects.TimelineCircleEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.timetravel.CombatStateTree;
import theRetrospect.timetravel.StateManager;
import theRetrospect.util.TimelineUtils;

public class RewindAction extends AbstractGameAction {

    public static final String ID = RetrospectMod.makeID(AbstractBaseCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(RetrospectMod.makeID(RewindAction.class.getSimpleName()));

    private final int rounds;
    private final AbstractCard rewindCard;
    private boolean firstFrame = true;

    public RewindAction(AbstractCard rewindCard, int rounds) {
        this.rewindCard = rewindCard;
        this.rounds = rounds;
        this.duration = Settings.FAST_MODE ? 0.5f : 1f;
    }

    @Override
    public void update() {
        if (firstFrame) {
            firstFrame = false;
            AbstractPlayer player = AbstractDungeon.player;

            if (MinionUtils.getMinions(player).monsters.size() >= MinionUtils.getMaxMinionCount(player)) {
                AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[1], true));
                this.isDone = true;
                return;
            }

            CombatStateTree.LinearPath timelinePath = StateManager.rewindTime(rounds, rewindCard);

            TimelineMinion minion = new TimelineMinion(player, (int) (-Settings.WIDTH * 0.5), 0, timelinePath);
            MinionUtils.addMinion(player, minion);
            TimelineUtils.timelinesConstructedThisCombat.add(minion);

            TimelineUtils.repositionTimelines(player);

            for (int i = 0; i < minion.cards.size() && i < 10; i++) {
                AbstractDungeon.effectList.add(new FlyingOrbEffect(
                        minion.target_x, minion.target_y + minion.hb.height / 2,
                        player.hb.cX, player.hb.cY,
                        new Color(0.7f, 0.5f, 0.8f, 0.4f)
                ));
            }
            AbstractDungeon.effectList.add(new TimelineCircleEffect(minion));
            AbstractDungeon.topLevelEffects.add(new TimeTravelEffect());
            CardCrawlGame.sound.play("CARD_POWER_IMPACT", 0.1f);

            if (!TipTracker.tips.get(TimelineUtils.REWIND_TIP)) {
                AbstractDungeon.ftue = new FtueTip(
                        tutorialStrings.LABEL[0],
                        tutorialStrings.TEXT[0],
                        minion.target_x + minion.hb.width / 2 + 300,
                        minion.target_y,
                        FtueTip.TipType.CREATURE
                );
                ReflectionHacks.setPrivate(AbstractDungeon.ftue, AbstractDungeon.ftue.getClass(), "m", minion);

                TipTracker.neverShowAgain(TimelineUtils.REWIND_TIP);
            }

            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof TimelineConstructSubscriber) {
                    TimelineConstructSubscriber listener = (TimelineConstructSubscriber) power;
                    listener.afterTimelineConstruct(minion);
                }
            }

            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof TimelineConstructSubscriber) {
                    TimelineConstructSubscriber listener = (TimelineConstructSubscriber) relic;
                    listener.afterTimelineConstruct(minion);
                }
            }
        }
        tickDuration();
    }
}
