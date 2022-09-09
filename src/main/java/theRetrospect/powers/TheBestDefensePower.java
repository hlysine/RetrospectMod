package theRetrospect.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.subscribers.CreatureBlockedDamageSubscriber;
import theRetrospect.util.TextureLoader;

public class TheBestDefensePower extends AbstractPower implements CreatureBlockedDamageSubscriber {

    public static final String POWER_ID = RetrospectMod.makeID(TheBestDefensePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/the_best_defense84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/the_best_defense32.png");

    public TheBestDefensePower(final AbstractCreature owner, final int amount) {
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
    public void onBlockedDamage(DamageInfo damageInfo, int damageAmount, int amountBlocked) {
        if (damageInfo.owner != null && damageInfo.owner != this.owner) {
            if (amountBlocked > 0 || this.owner.currentBlock > 0)
                addToTop(new DamageAction(damageInfo.owner, new DamageInfo(this.owner, amountBlocked, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SMASH));
        }
    }

    @Override
    public void atEndOfRound() {
        if (this.amount > 1) {
            addToBot(new ReducePowerAction(this.owner, this.owner, ID, 1));
        } else {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}