package theRetrospect.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.curses.Singularity;
import theRetrospect.relics.BottledSingularity;

import static theRetrospect.RetrospectMod.makeEventPath;

public class EventHorizonEvent extends PhasedEvent {
    public static final String ID = RetrospectMod.makeID(EventHorizonEvent.class.getSimpleName());
    public static final String IMG = makeEventPath("placeholder_event_1.png");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String TITLE = eventStrings.NAME;

    public EventHorizonEvent() {
        super(ID, TITLE, IMG);

        registerPhase("Encounter", new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(OPTIONS[0], new Singularity(), new BottledSingularity()), (i) -> {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, new BottledSingularity());
                    transitionKey("Capture");
                })
                .addOption(OPTIONS[1], (i) -> transitionKey("Escape"))
        );

        registerPhase("Capture", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[2], i -> openMap())
        );

        registerPhase("Escape", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[2], i -> openMap())
        );

        transitionKey("Encounter");
    }
}
