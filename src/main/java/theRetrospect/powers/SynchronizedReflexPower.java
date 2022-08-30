package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TextureLoader;
import theRetrospect.util.TimelineUtils;

public class SynchronizedReflexPower extends AbstractPower implements CloneablePowerInterface {
    public final AbstractPlayer player;

    public static final String POWER_ID = RetrospectMod.makeID(SynchronizedReflexPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public SynchronizedReflexPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        if (owner instanceof AbstractPlayer)
            this.player = (AbstractPlayer) owner;
        else
            this.player = null;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.player == null) return damageAmount;
        if (info.type != DamageInfo.DamageType.THORNS &&
                info.type != DamageInfo.DamageType.HP_LOSS &&
                info.owner != null &&
                info.owner != this.owner &&
                damageAmount > 0) {
            boolean flashed = false;
            for (int i = 0; i < this.amount; i++) {
                TimelineMinion timeline = TimelineUtils.getRandomTimeline((AbstractPlayer) this.owner, t -> t.cards.size() > 0);
                if (timeline != null) {
                    if (!flashed) {
                        flash();
                        flashed = true;
                    }
                    addToTop(new ApplyPowerAction(timeline, owner, new TimerPower(timeline, 1)));
                }
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        if (this.player != null) {
            description = DESCRIPTIONS[0] + describeNumber(this.amount, 1);
        } else {
            description = DESCRIPTIONS[3];
        }
    }

    private String describeNumber(int number, int singularIndex) {
        if (number > 1) return number + DESCRIPTIONS[singularIndex + 1];
        else return number + DESCRIPTIONS[singularIndex];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SynchronizedReflexPower(owner, amount);
    }
}
