package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.CollapseTimelineAction;
import theRetrospect.actions.NonTriggeringHealthChange;
import theRetrospect.actions.QueueCardIntentAction;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.HoverableCardStack;
import theRetrospect.util.TextureLoader;

public class TimerPower extends AbstractPower implements CloneablePowerInterface, EndOfTurnCardSubscriber {
    public AbstractMinionWithCards minion;

    public static final String POWER_ID = RetrospectMod.makeID(TimerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public TimerPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        if (owner instanceof AbstractMinionWithCards)
            this.minion = (AbstractMinionWithCards) owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    @Override
    public void onInitialApplication() {
        updateDescription();
        updateCardIntents();
    }

    public void extraReplay() {
        for (int i = 0; i < amount; i++) {
            if (minion.cards.size() <= i) break;
            AbstractCard cardToPlay = minion.cards.get(i).makeStatEquivalentCopy();
            replayCard(cardToPlay, null);
        }
    }

    @Override
    public void endOfTurnPlayCards() {
        for (int i = 0; i < amount; i++) {
            if (minion.cards.size() <= 0) break;
            AbstractCard cardToPlay = minion.cards.remove(0);
            replayCard(cardToPlay, minion.cardStack);
        }
        updateDescription();
        updateCardIntents();
        if (minion.cards.size() == 0) {
            AbstractDungeon.actionManager.addToBottom(new CollapseTimelineAction(minion));
        }
    }

    private void replayCard(AbstractCard cardToPlay, HoverableCardStack cardStack) {
        addToBot(new QueueCardIntentAction(cardToPlay, cardStack, true));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
        updateCardIntents();
    }

    @Override
    public void onVictory() {
        new NonTriggeringHealthChange(AbstractDungeon.player, minion.currentHealth).update();
        new InstantKillAction(minion).update();
    }

    private void updateCardIntents() {
        minion.cardIntents.clear();
        for (int i = 0; i < amount; i++) {
            if (i >= minion.cards.size()) break;
            minion.cardIntents.add(minion.cards.get(i));
        }
    }

    @Override
    public void updateDescription() {
        if (minion == null)
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
