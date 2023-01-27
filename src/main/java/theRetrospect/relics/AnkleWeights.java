package theRetrospect.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import theRetrospect.RetrospectMod;
import theRetrospect.patches.relics.CampfireRelicsPatch;

import java.util.ArrayList;

public class AnkleWeights extends AbstractBaseRelic {

    public static final String ID = RetrospectMod.makeID(AnkleWeights.class.getSimpleName());

    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;
    public static final int TRAIN_LIMIT = 3;

    public AnkleWeights() {
        super(ID, LANDING_SOUND);
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0) {
            flash();
            addToBot(new DrawCardAction(AbstractDungeon.player, this.counter));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public boolean canSpawn() {
        if (AbstractDungeon.floorNum >= 48 && !Settings.isEndless) {
            return false;
        }

        return CampfireRelicsPatch.canRelicSpawn();
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new TrainOption(this.counter < TRAIN_LIMIT));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
