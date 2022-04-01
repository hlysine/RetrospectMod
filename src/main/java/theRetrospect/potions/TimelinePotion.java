package theRetrospect.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ConstructTimelineAction;

public class TimelinePotion extends CustomPotion {

    public static final String POTION_ID = RetrospectMod.makeID(TimelinePotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID_COLOR = CardHelper.getColor(135, 50, 200);
    public static final Color HYBRID_COLOR = CardHelper.getColor(200, 170, 220);
    public static final Color SPOTS_COLOR = CardHelper.getColor(60, 25, 150);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
    private static final PotionSize SIZE = PotionSize.SPHERE;
    private static final PotionColor COLOR = PotionColor.ELIXIR;

    private static final int HEALTH_COST = 20;

    public TimelinePotion() {
        super(NAME, POTION_ID, RARITY, SIZE, COLOR);

        potency = getPotency();

        description = DESCRIPTIONS[0];

        isThrown = false;

        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new ConstructTimelineAction(HEALTH_COST));
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) return false;
        return AbstractDungeon.player.currentHealth > HEALTH_COST;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TimelinePotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 1;
    }
}