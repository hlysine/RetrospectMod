package theRetrospect.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import theRetrospect.RetrospectMod;
import theRetrospect.effects.CampfireTrainEffect;
import theRetrospect.util.TextureLoader;

public class TrainOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(RetrospectMod.makeID(TrainOption.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;

    private static final String IMG = RetrospectMod.makeUiPath("train.png");

    public TrainOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.description = active ? TEXT[1] : TEXT[2];
        this.img = TextureLoader.getTexture(IMG);
    }


    public void useOption() {
        if (this.usable)
            AbstractDungeon.effectList.add(new CampfireTrainEffect());
    }
}