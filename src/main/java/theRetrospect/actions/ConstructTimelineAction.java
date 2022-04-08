package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractTimelineCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineCollapseSubscriber;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.MinionUtils;
import theRetrospect.util.TimelineUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructTimelineAction extends AbstractGameAction {

    public static final String ID = RetrospectMod.makeID(AbstractTimelineCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final float HEALTH_PERCENTAGE_COST = 0.4f;

    private final boolean overrideHealthCost;
    private final int healthBorrowed;

    public ConstructTimelineAction(int healthBorrowed) {
        this.overrideHealthCost = true;
        this.healthBorrowed = healthBorrowed;
    }

    public ConstructTimelineAction() {
        this.overrideHealthCost = false;
        this.healthBorrowed = 0;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;

        if (MinionUtils.getMinions(player).monsters.size() >= MinionUtils.getMaxMinions(player)) {
            AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[1], true));
            this.isDone = true;
            return;
        }

        int health = calculateHealthBorrowed(player);

        if (player.currentHealth > health) {
            TimelineMinion minion = new TimelineMinion(
                    filterReplayableCards(AbstractDungeon.actionManager.cardsPlayedThisTurn),
                    (int) (-Settings.WIDTH * 0.5), 0, health);

            MinionUtils.addMinion(player, minion);

            TimelineUtils.repositionTimelines(player);

            AbstractDungeon.actionManager.addToTop(new NonTriggeringHealthChange(player, -health));

            AbstractDungeon.actionManager.addToTop(new VFXAction(new EmpowerEffect(minion.drawX, minion.drawY)));

            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof TimelineConstructSubscriber) {
                    TimelineConstructSubscriber listener = (TimelineConstructSubscriber) relic;
                    listener.onTimelineConstruct(minion);
                }
            }
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[0], true));
        }

        this.isDone = true;
    }

    private int calculateHealthBorrowed(AbstractPlayer player) {
        int ret;

        if (overrideHealthCost)
            ret = healthBorrowed;
        else
            ret = (int) Math.ceil(player.currentHealth * HEALTH_PERCENTAGE_COST);

        return Math.max(1, Math.min(player.currentHealth, ret));
    }

    private List<AbstractCard> filterReplayableCards(List<AbstractCard> cards) {
        return cards.stream()
                .filter(CardUtils::isCardReplayable)
                .map(CardUtils::makeAdvancedCopy)
                .collect(Collectors.toList());
    }
}
