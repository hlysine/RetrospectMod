package theRetrospect.events;


import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theRetrospect.RetrospectMod;

import java.util.ArrayList;
import java.util.List;

/**
 * A nerfed version of the base game Council of Ghosts event.
 * The Retrospect will receive 3(2) instead of 5(3) Apparitions.
 */
public class Ghosts extends AbstractImageEvent {
    public static final String ID = "Ghosts";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(RetrospectMod.makeID(Ghosts.class.getSimpleName()));
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_BODY_M = DESCRIPTIONS[0];
    private static final String ACCEPT_BODY = DESCRIPTIONS[2];
    private static final String EXIT_BODY = DESCRIPTIONS[3];
    private static final float HP_DRAIN = 0.5F;
    private int screenNum = 0, hpLoss;

    public Ghosts() {
        super(NAME, "test", "images/events/ghost.jpg");
        this.body = INTRO_BODY_M;

        this.hpLoss = MathUtils.ceil(AbstractDungeon.player.maxHealth * HP_DRAIN);
        if (this.hpLoss >= AbstractDungeon.player.maxHealth) {
            this.hpLoss = AbstractDungeon.player.maxHealth - 1;
        }

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.imageEventText.setDialogOption(OPTIONS[3] + this.hpLoss + OPTIONS[1], new Apparition());
        } else {
            this.imageEventText.setDialogOption(OPTIONS[0] + this.hpLoss + OPTIONS[1], new Apparition());
        }

        this.imageEventText.setDialogOption(OPTIONS[2]);
    }

    public void onEnterRoom() {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.play("EVENT_GHOSTS");
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screenNum) {
            case 0:
                if (buttonPressed == 0) {
                    this.imageEventText.updateBodyText(ACCEPT_BODY);

                    AbstractDungeon.player.decreaseMaxHealth(this.hpLoss);

                    becomeGhost();

                    this.screenNum = 1;
                    this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                    this.imageEventText.clearRemainingOptions();
                    return;
                }

                logMetricIgnored("Ghosts");
                this.imageEventText.updateBodyText(EXIT_BODY);
                this.screenNum = 2;
                this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                this.imageEventText.clearRemainingOptions();
                return;

            case 1:
                openMap();
                return;
        }

        openMap();
    }


    private void becomeGhost() {
        List<String> cards = new ArrayList<>();
        int amount = 3;
        if (AbstractDungeon.ascensionLevel >= 15) {
            amount -= 1;
        }
        for (int i = 0; i < amount; i++) {
            Apparition apparition = new Apparition();
            cards.add(apparition.cardID);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(apparition, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }

        logMetricObtainCardsLoseMapHP("Ghosts", "Became a Ghost", cards, this.hpLoss);
    }
}
