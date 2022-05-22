package theRetrospect.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.util.TextureLoader;

public class DefectiveEchoPower extends AbstractPower {

    public static final String POWER_ID = RetrospectMod.makeID(DefectiveEchoPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    private boolean justApplied;

    public DefectiveEchoPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;
        justApplied = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (justApplied) {
            justApplied = false;
        }
        updateDescription();
    }

    public void updateDescription() {
        if (this.justApplied) {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0];
            } else {
                this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
            }
        } else if (this.amount == 1) {
            this.description = DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[4] + this.amount + DESCRIPTIONS[5];
        }
    }

    @Override
    public void atEndOfRound() {
        if (this.justApplied) return;
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.justApplied && !card.purgeOnUse && this.amount > 0) {
            flash();
            AbstractMonster m = null;

            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;

            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            addToBot(new ReducePowerAction(this.owner, this.owner, ID, 1));
        }
    }
}