package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.QueueCardIntentAction;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.actions.timelineActions.CollapseTimelineAction;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.subscribers.BeforeMinionPlayCardSubscriber;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.subscribers.MinionCardsChangedSubscriber;
import theRetrospect.util.*;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerPower extends AbstractPower implements CloneablePowerInterface, EndOfTurnCardSubscriber, MinionCardsChangedSubscriber {
    public final AbstractMinionWithCards minion;

    public static final String POWER_ID = RetrospectMod.makeID(TimerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/timer84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/timer32.png");

    public TimerPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        if (owner instanceof AbstractMinionWithCards)
            this.minion = (AbstractMinionWithCards) owner;
        else
            this.minion = null;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    @Override
    public void onInitialApplication() {
        onCardsChanged();
    }

    public void triggerWithoutConsumingCards(Runnable next) {
        if (minion == null) {
            next.run();
            return;
        }
        AtomicInteger cardIdx = new AtomicInteger(0);
        CallbackUtils.ForLoop(
                () -> {
                    if (minion.cards.size() == 0) return false;
                    int idx = cardIdx.get();
                    if (minion.cards.size() <= idx) return false;
                    if (idx >= amount) return false;
                    return !minion.isDeadOrEscaped();
                },
                () -> {
                    int i = cardIdx.decrementAndGet();
                    if (i < amount - 1)
                        addToBot(new WaitAction(0.75f));
                },
                nxt -> {
                    AbstractCard cardToPlay = CardUtils.makeAdvancedCopy(minion.cards.get(cardIdx.get()), true);
                    CardUtils.addFollowUpActionToBottom(cardToPlay, new RunnableAction(nxt), true, 0);
                    playCard(cardToPlay, null);
                },
                next
        );
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard(Runnable next) {
        if (minion == null) {
            next.run();
            return;
        }
        AtomicInteger remainingAmount = new AtomicInteger(amount);
        CallbackUtils.ForLoop(
                () -> {
                    updateDescription();
                    updateCardIntents();
                    if (minion.cards.size() == 0) {
                        addToBot(new CollapseTimelineAction(minion));
                        return false;
                    }
                    if (remainingAmount.get() <= 0) return false;
                    return !minion.isDeadOrEscaped();
                },
                () -> {
                    int i = remainingAmount.decrementAndGet();
                    if (i > 0)
                        addToBot(new WaitAction(0.75f));
                },
                nxt -> {
                    AbstractCard cardToPlay = minion.cards.remove(0);
                    CardUtils.addFollowUpActionToBottom(cardToPlay, new RunnableAction(nxt), true, 0);
                    CardUtils.addFollowUpActionToTop(cardToPlay, new RunnableAction(() -> {
                        updateDescription();
                        updateCardIntents();
                    }), false, 1000);
                    playCard(cardToPlay, minion.cardStack);
                },
                next
        );
    }

    private void playCard(AbstractCard cardToPlay, HoverableCardStack cardStack) {
        for (AbstractPower p : this.owner.powers) {
            if (p instanceof BeforeMinionPlayCardSubscriber) {
                BeforeMinionPlayCardSubscriber beforeMinionPlayCardSubscriber = (BeforeMinionPlayCardSubscriber) p;
                beforeMinionPlayCardSubscriber.beforeMinionPlayCard(this.minion, cardToPlay);
            }
        }
        addToBot(new QueueCardIntentAction(cardToPlay, cardStack, CardPlaySource.TIMELINE, false));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
        updateCardIntents();
    }

    @Override
    public void onVictory() {
        if (minion != null && !minion.isDeadOrEscaped())
            TimelineUtils.instantCollapseWithoutEffect(minion);
    }

    @Override
    public void onCardsChanged() {
        updateDescription();
        updateCardIntents();
    }

    private void updateCardIntents() {
        if (minion == null) return;
        minion.cardIntents.clear();
        for (int i = 0; i < amount; i++) {
            if (i >= minion.cards.size()) break;
            minion.cardIntents.add(minion.cards.get(i));
        }
    }

    @Override
    public void updateDescription() {
        if (minion == null)
            description = DESCRIPTIONS[6];
        else if (minion.cards.size() == 0)
            description = DESCRIPTIONS[5];
        else
            description = DESCRIPTIONS[0] + Math.min(amount, minion.cards.size()) + DESCRIPTIONS[1] + describeNumber(minion.cards.size(), 2) + DESCRIPTIONS[4];
    }

    private String describeNumber(int number, int singularIndex) {
        if (number > 1) return number + DESCRIPTIONS[singularIndex + 1];
        else return number + DESCRIPTIONS[singularIndex];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimerPower(owner, amount);
    }
}
