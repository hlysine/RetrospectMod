package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.subscribers.OnDeathPreProtectionSubscriber;
import theRetrospect.util.TextureLoader;
import theRetrospect.util.TimelineUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DimensionalRiftPower extends AbstractPower implements CloneablePowerInterface, OnDeathPreProtectionSubscriber {
    public final AbstractPlayer player;

    public static final String POWER_ID = RetrospectMod.makeID(DimensionalRiftPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public DimensionalRiftPower(final AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = -1;
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
    public void updateDescription() {
        if (player != null) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1];
        }
    }

    @Override
    public boolean onDeathPreProtection(DamageInfo damageInfo, DeathInfo deathInfo, boolean canDie) {
        if (player == null) return true;
        if (canDie) {
            List<TimelineMinion> timelines = TimelineUtils.getTimelines(player);
            Optional<TimelineMinion> target = timelines.stream().max(Comparator.comparingInt(t -> t.currentHealth));
            if (target.isPresent()) {
                flash();
                TimelineUtils.instantCollapseWithEffect(target.get());
            }
            player.currentHealth -= (deathInfo.finalHPDamage - deathInfo.healthBeforeDamage);
            return player.currentHealth < 0;
        }
        return true;
    }

    @Override
    public AbstractPower makeCopy() {
        return new DimensionalRiftPower(owner);
    }
}
