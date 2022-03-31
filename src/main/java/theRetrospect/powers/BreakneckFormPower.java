package theRetrospect.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.SpeedUpTimelineAction;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;
import theRetrospect.util.TextureLoader;

import java.util.List;
import java.util.stream.Collectors;

public class BreakneckFormPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = RetrospectMod.makeID(BreakneckFormPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theRetrospectResources/images/powers/placeholder_power32.png");

    public BreakneckFormPower(final AbstractCreature owner, final int amount) {
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
    public void atStartOfTurn() {
        List<TimelineMinion> timelines = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());
        if (timelines.size() == 0) return;
        for (int i = 0; i < this.amount; i++) {
            TimelineMinion target = timelines.get(AbstractDungeon.cardRng.random(timelines.size() - 1));
            addToBot(new SpeedUpTimelineAction(target, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BreakneckFormPower(owner, amount);
    }
}
