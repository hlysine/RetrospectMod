package theRetrospect.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.skills.Divert;

import static theRetrospect.RetrospectMod.makeEventPath;

public class FutureGadgetsEvent extends PhasedEvent {
    public static final String ID = RetrospectMod.makeID(FutureGadgetsEvent.class.getSimpleName());
    public static final String IMG = makeEventPath("placeholder_event_1.png");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String TITLE = eventStrings.NAME;

    public FutureGadgetsEvent() {
        super(ID, TITLE, IMG);

        registerPhase("Encounter", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0], new Divert()), (i) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Divert(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    transitionKey("Yes");
                })
                .addOption(OPTIONS[1], (i) -> transitionKey("No"))
        );

        registerPhase("Yes", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2], i -> openMap())
        );

        registerPhase("No", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[2], i -> openMap())
        );

        transitionKey("Encounter");
    }
}
