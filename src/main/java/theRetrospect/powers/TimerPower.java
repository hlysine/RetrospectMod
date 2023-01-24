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
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.QueueCardIntentAction;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.subscribers.BeforeMinionPlayCardSubscriber;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.*;

public class TimerPower extends AbstractPower implements CloneablePowerInterface, EndOfTurnCardSubscriber {
    public final AbstractMinionWithCards minion;

    public static final String POWER_ID = RetrospectMod.makeID(TimerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/timer84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/timer32.png");

    public TimerPower(final AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;
        if (owner instanceof AbstractMinionWithCards)
            this.minion = (AbstractMinionWithCards) owner;
        else
            this.minion = null;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard(Runnable next) {
        if (minion == null || minion.isDeadOrEscaped()) {
            next.run();
            return;
        }
        CallbackUtils.ForLoop(
                () -> {
                    if (minion.cards.size() == 0) return false;
                    AbstractCard cardToPlay = minion.cards.get(0);
                    if (EnergyPanel.getCurrentEnergy() < cardToPlay.costForTurn) {
                        return false;
                    }
                    return !minion.isDeadOrEscaped();
                },
                () -> {
                    if (minion.cards.size() == 0) return;
                    AbstractCard cardToPlay = minion.cards.get(0);
                    if (EnergyPanel.getCurrentEnergy() >= cardToPlay.costForTurn) {
                        addToBot(new WaitAction(0.75f));
                    }
                },
                nxt -> {
                    AbstractCard cardToPlay = minion.cards.remove(0);
                    CardUtils.addFollowUpActionToBottom(cardToPlay, new RunnableAction(nxt), true, 0);
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
        addToBot(new QueueCardIntentAction(cardToPlay, cardStack, CardPlaySource.TIMELINE, false, false));
    }

    @Override
    public void updateDescription() {
        if (minion == null)
            description = DESCRIPTIONS[1];
        else
            description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimerPower(owner);
    }
}
