package theRetrospect.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.DefectiveEcho;
import theRetrospect.cards.DivineEye;
import theRetrospect.cards.NoxiousWraith;
import theRetrospect.cards.SoullessDemon;

import static theRetrospect.RetrospectMod.makeEventPath;

public class MysteriousProphetEvent extends PhasedEvent {
    public static final String ID = RetrospectMod.makeID(MysteriousProphetEvent.class.getSimpleName());
    public static final String IMG = makeEventPath("placeholder_event_1.png");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String TITLE = eventStrings.NAME;

    public MysteriousProphetEvent() {
        super(ID, TITLE, IMG);

        registerPhase("Encounter", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], i -> transitionKey("Prophet's Words"))
        );

        registerPhase("Prophet's Words", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[1], (i) -> transitionKey("Offer"))
                .addOption(OPTIONS[2], (i) -> transitionKey("Rejection"))
        );

        registerPhase("Rejection", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[3], i -> openMap())
        );

        registerPhase("Offer", new TextPhase(DESCRIPTIONS[3])
                .addOption(new TextPhase.OptionInfo(OPTIONS[4], new SoullessDemon()), (i) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new SoullessDemon(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[5], new NoxiousWraith()), (i) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new NoxiousWraith(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[6], new DefectiveEcho()), (i) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DefectiveEcho(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[7], new DivineEye()), (i) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DivineEye(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    transitionKey("Complete");
                })
        );

        registerPhase("Complete", new TextPhase(DESCRIPTIONS[4])
                .addOption(OPTIONS[3], (i) -> openMap())
        );

        transitionKey("Encounter");
    }
}
