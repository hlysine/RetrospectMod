package theRetrospect.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.skills.DefectiveEcho;
import theRetrospect.cards.skills.DivineEye;
import theRetrospect.cards.skills.NoxiousWraith;
import theRetrospect.cards.skills.SoullessDemon;

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
                .addOption(OPTIONS[2], (i) -> {
                    logMetricIgnored(ID);
                    transitionKey("Rejection");
                })
        );

        registerPhase("Rejection", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[3], i -> openMap())
        );

        registerPhase("Offer", new TextPhase(DESCRIPTIONS[3])
                .addOption(new TextPhase.OptionInfo(OPTIONS[4], new SoullessDemon()), (i) -> {
                    AbstractCard card = new SoullessDemon();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    logMetricObtainCard(ID, "Red", card);
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[5], new NoxiousWraith()), (i) -> {
                    AbstractCard card = new NoxiousWraith();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    logMetricObtainCard(ID, "Green", card);
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[6], new DefectiveEcho()), (i) -> {
                    AbstractCard card = new DefectiveEcho();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    logMetricObtainCard(ID, "Blue", card);
                    transitionKey("Complete");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[7], new DivineEye()), (i) -> {
                    AbstractCard card = new DivineEye();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                    logMetricObtainCard(ID, "Purple", card);
                    transitionKey("Complete");
                })
        );

        registerPhase("Complete", new TextPhase(DESCRIPTIONS[4])
                .addOption(OPTIONS[3], (i) -> openMap())
        );

        transitionKey("Encounter");
    }
}
