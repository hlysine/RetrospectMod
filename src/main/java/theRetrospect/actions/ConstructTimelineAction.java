package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TimelineUtils;

import java.util.stream.Collectors;

public class ConstructTimelineAction extends AbstractGameAction {

    public static final String ID = RetrospectMod.makeID(AbstractRetrospectCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final float HEALTH_PERCENTAGE_COST = 0.4f;

    private final AbstractCard constructionCard;

    public ConstructTimelineAction(AbstractCard constructionCard) {
        this.constructionCard = constructionCard;
    }

    @Override
    public void update() {
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
                            .map(CardUtils::makeAdvancedCopy)
                            .collect(Collectors.toList()),
                    (int) (-Settings.WIDTH * 0.5), 0, health);

            MinionUtils.addMinion(player, minion);

            TimelineUtils.repositionTimelines(player);

            AbstractDungeon.actionManager.addToTop(new NonTriggeringHealthChange(player, -health));

            AbstractDungeon.actionManager.addToTop(new VFXAction(new EmpowerEffect(minion.drawX, minion.drawY)));

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

        this.isDone = true;
    }

    private int calculateHealthBorrowed(AbstractPlayer player) {
        float percentage = HEALTH_PERCENTAGE_COST;

        for (AbstractPower power : player.powers) {
            if (power instanceof TimelineConstructSubscriber) {
                TimelineConstructSubscriber listener = (TimelineConstructSubscriber) power;
                percentage = listener.modifyTimelineConstruct(constructionCard, percentage);
            }
        }

        for (AbstractRelic relic : player.relics) {
            if (relic instanceof TimelineConstructSubscriber) {
                TimelineConstructSubscriber listener = (TimelineConstructSubscriber) relic;
                percentage = listener.modifyTimelineConstruct(constructionCard, percentage);
            }
        }

        int ret = (int) Math.ceil(player.currentHealth * percentage);
        return Math.max(1, Math.min(player.currentHealth, ret));
    }
}
