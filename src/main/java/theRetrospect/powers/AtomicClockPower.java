package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.CustomQueueCardAction;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.actions.general.ShowCardToBePlayedAction;
import theRetrospect.cards.skills.Avert;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TextureLoader;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicClockPower extends AbstractPower implements CloneablePowerInterface, EndOfTurnCardSubscriber {

    public static final String POWER_ID = RetrospectMod.makeID(AtomicClockPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public AtomicClockPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard(Runnable next) {
        AtomicInteger remainingAmount = new AtomicInteger(amount);
        CallbackUtils.ForLoop(
                () -> remainingAmount.get() > 0,
                () -> {
                    int i = remainingAmount.decrementAndGet();
                    if (i > 0)
                        addToBot(new WaitAction(0.75f));
                },
                nxt -> {
                    AbstractCard card = new Avert();
                    card.applyPowers();
                    card.purgeOnUse = true;
                    card.current_x = card.target_x = AbstractDungeon.player.drawX;
                    card.current_y = card.target_y = AbstractDungeon.player.drawY;
                    CardUtils.addFollowUpActionToBottom(card, new RunnableAction(nxt), true);
                    addToBot(new ShowCardToBePlayedAction(card));
                    addToBot(new CustomQueueCardAction(card, true, false, true));
                    addToBot(new ReducePowerAction(owner, owner, this, 1));
                },
                next
        );
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new AtomicClockPower(owner, amount);
    }
}