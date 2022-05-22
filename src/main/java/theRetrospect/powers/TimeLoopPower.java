package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.subscribers.BeforeMinionPlayCardSubscriber;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TextureLoader;

public class TimeLoopPower extends AbstractPower implements CloneablePowerInterface, BeforeMinionPlayCardSubscriber {

    public static final String POWER_ID = RetrospectMod.makeID(TimeLoopPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public TimeLoopPower(final AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void beforeMinionPlayCard(AbstractMinionWithCards timeline, AbstractCard card) {
        if (timeline == this.owner) {
            CardUtils.setReturnToMinion(card, timeline);
            CardUtils.addFollowUpActionToTop(card, new VFXAction(new FlashPowerEffect(this)));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimeLoopPower(owner);
    }
}
