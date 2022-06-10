package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructTimelineAction;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.TextureLoader;

public class ShortcutPower extends AbstractPower implements CloneablePowerInterface, TimelineConstructSubscriber {

    public static final String POWER_ID = RetrospectMod.makeID(ShortcutPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    private static final int MAX_AMOUNT = (int) (ConstructTimelineAction.HEALTH_PERCENTAGE_COST * 100f);

    public ShortcutPower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = Math.min(MAX_AMOUNT, amount);

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.min(MAX_AMOUNT, this.amount + stackAmount);
    }

    @Override
    public float modifyTimelineHP(AbstractCard constructionCard, float healthPercentageCost) {
        return Math.max(0.01f, healthPercentageCost - this.amount / 100f);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShortcutPower(owner, amount);
    }
}
