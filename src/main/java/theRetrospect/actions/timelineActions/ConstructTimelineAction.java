package theRetrospect.actions.timelineActions;

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
import theRetrospect.actions.general.NonTriggeringHealthChange;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.effects.FlyingOrbEffect;
import theRetrospect.effects.TimelineCircleEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TimelineUtils;

import java.util.stream.Collectors;

public class ConstructTimelineAction extends AbstractGameAction {

    public static final String ID = RetrospectMod.makeID(AbstractBaseCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(RetrospectMod.makeID(ConstructTimelineAction.class.getSimpleName()));

    public static final float HEALTH_PERCENTAGE_COST = 0.4f;

    private final AbstractCard constructionCard;
    private boolean isConstructing = true;

    public ConstructTimelineAction(AbstractCard constructionCard) {
        this.constructionCard = constructionCard;
        this.duration = Settings.FAST_MODE ? 0.5f : 1f;
    }

    @Override
    public void update() {
        if (isConstructing) {
            isConstructing = false;
            AbstractPlayer player = AbstractDungeon.player;

            if (MinionUtils.getMinions(player).monsters.size() >= MinionUtils.getMaxMinionCount(player)) {
                AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[1], true));
                this.isDone = true;
                return;
            }

            int health = calculateHealthBorrowed(player);

            if (player.currentHealth > health) {
                TimelineMinion minion = new TimelineMinion(
                        player,
                        AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                                .filter(card -> card != constructionCard)
                                .map(card -> CardUtils.makeAdvancedCopy(card, true))
                                .collect(Collectors.toList()),
                        (int) (-Settings.WIDTH * 0.5), 0, health);

                MinionUtils.addMinion(player, minion);
                TimelineUtils.timelinesConstructedThisCombat.add(minion);

                TimelineUtils.repositionTimelines(player);

                addToTop(new NonTriggeringHealthChange(player, -health));

                for (int i = 0; i < minion.cards.size() && i < 10; i++) {
                    AbstractDungeon.effectList.add(new FlyingOrbEffect(
                            player.hb.cX, player.hb.cY,
                            minion.target_x, minion.target_y + minion.hb.height / 2,
                            new Color(0.7f, 0.5f, 0.8f, 0.4f)
                    ));
                }

                CardCrawlGame.sound.play("CARD_POWER_IMPACT", 0.1f);

                AbstractDungeon.effectList.add(new TimelineCircleEffect(minion));

                if (!TipTracker.tips.get(TimelineUtils.TIMELINE_TIP)) {
                    AbstractDungeon.ftue = new FtueTip(
                            tutorialStrings.LABEL[0],
                            tutorialStrings.TEXT[0],
                            minion.target_x + minion.hb.width / 2 + 300,
                            minion.target_y,
                            FtueTip.TipType.CREATURE
                    );
                    ReflectionHacks.setPrivate(AbstractDungeon.ftue, AbstractDungeon.ftue.getClass(), "m", minion);

                    TipTracker.neverShowAgain(TimelineUtils.TIMELINE_TIP);
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
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[0], true));
            }
        }

        tickDuration();
    }

    private int calculateHealthBorrowed(AbstractPlayer player) {
        float percentage = HEALTH_PERCENTAGE_COST;

        for (AbstractPower power : player.powers) {
            if (power instanceof TimelineConstructSubscriber) {
                TimelineConstructSubscriber listener = (TimelineConstructSubscriber) power;
                percentage = listener.modifyTimelineHP(constructionCard, percentage);
            }
        }

        for (AbstractRelic relic : player.relics) {
            if (relic instanceof TimelineConstructSubscriber) {
                TimelineConstructSubscriber listener = (TimelineConstructSubscriber) relic;
                percentage = listener.modifyTimelineHP(constructionCard, percentage);
            }
        }

        int ret = (int) Math.ceil(player.currentHealth * percentage);
        return Math.max(1, Math.min(player.currentHealth, ret));
    }
}
