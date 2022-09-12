package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.TimelineConstructSubscriber;
import theRetrospect.util.TextureLoader;

public class SmartCollarPower extends AbstractPower implements CloneablePowerInterface, TimelineConstructSubscriber {

    public static final String POWER_ID = RetrospectMod.makeID(SmartCollarPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/smart_collar84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/smart_collar32.png");

    public SmartCollarPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float modifyTimelineHP(AbstractCard constructionCard, float healthPercentageCost) {
        flash();
        return 0;
    }

    @Override
    public void afterTimelineConstruct(TimelineMinion timeline) {
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SmartCollarPower(owner, amount);
    }
}
