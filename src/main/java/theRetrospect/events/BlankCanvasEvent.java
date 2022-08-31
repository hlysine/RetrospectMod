package theRetrospect.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.SwirlyBloodEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.wildcard.WildCard;
import theRetrospect.cards.wildcard.WildCardModifier;
import theRetrospect.util.DistributedRNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BlankCanvasEvent extends AbstractImageEvent {
    public static final String ID = RetrospectMod.makeID(BlankCanvasEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_BODY = DESCRIPTIONS[0];
    private static final List<String> CARD_BODY = Arrays.stream(DESCRIPTIONS).skip(1).limit(4).collect(Collectors.toList());
    private static final String IGNORE_BODY = DESCRIPTIONS[5];
    private static final String EXIT_BODY = DESCRIPTIONS[6];
    private static final String COMPLETE_BODY = DESCRIPTIONS[7];
    private static final int INITIAL_HP_LOSS = 4;
    private static final int HP_LOSS_INCREASE = 2;
    private int screenNum = 0, hpLoss = INITIAL_HP_LOSS;

    private final List<WildCardModifier> choices = WildCard.possibleEffects.stream().map(WildCardModifier::makeCopy).collect(Collectors.toList());
    private WildCard card = new WildCard();
    private List<WildCardModifier> currentChoices;
    private final List<List<WildCardModifier>> choiceHistory = new ArrayList<>();

    public BlankCanvasEvent() {
        super(NAME, "test", "images/events/ghost.jpg");
        this.body = INTRO_BODY;

        generateOptions();
    }

    protected void buttonEffect(int buttonPressed) {
        if (this.screenNum == -1) {
            openMap();
            return;
        }

        if (buttonPressed == currentChoices.size()) {
            showExitScreen();
            return;
        }

        this.screenNum++;

        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.hpLoss));
        AbstractDungeon.effectList.add(new SwirlyBloodEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));

        this.hpLoss += HP_LOSS_INCREASE;
        WildCardModifier newChoice = this.currentChoices.get(buttonPressed);
        this.choices.remove(newChoice);

        List<WildCardModifier> choiceRecord = new ArrayList<>(this.currentChoices);
        choiceRecord.remove(newChoice);
        choiceRecord.add(0, newChoice);
        this.choiceHistory.add(choiceRecord);

        List<WildCardModifier> modifiers = new ArrayList<>(card.modifiers);
        modifiers.add(newChoice);
        card = new WildCard(modifiers);

        if (choices.size() == 0) {
            showExitScreen();
            return;
        }

        generateOptions();
    }

    private void showExitScreen() {
        String playerChoice;
        if (this.screenNum == 0 && choices.size() > 0) {
            this.imageEventText.updateBodyText(IGNORE_BODY);
        } else if (choices.size() == 0) {
            this.imageEventText.updateBodyText(COMPLETE_BODY);
            this.currentChoices.clear();
        } else {
            this.imageEventText.updateBodyText(EXIT_BODY);
        }

        if (this.currentChoices.size() > 0) {
            ArrayList<WildCardModifier> finalChoices = new ArrayList<>(this.currentChoices);
            finalChoices.add(0, null);
            this.choiceHistory.add(finalChoices);
        }
        playerChoice = this.choiceHistory.stream()
                .map(list -> list.stream()
                        .map(mod -> mod == null ? "SKIP" : mod.getKey())
                        .collect(Collectors.joining(">"))
                )
                .collect(Collectors.joining("|"));

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card.makeStatEquivalentCopy(), Settings.WIDTH * 0.6F, Settings.HEIGHT / 2.0F));
        logMetricObtainCard(ID, playerChoice, card);
        this.screenNum = -1;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        this.imageEventText.clearRemainingOptions();
    }

    private void generateOptions() {
        if (this.screenNum == 0)
            this.imageEventText.updateBodyText(INTRO_BODY);
        else
            this.imageEventText.updateBodyText(
                    getCardDescriptionForDisplay(card) + " NL NL " +
                            CARD_BODY.get(Math.min(CARD_BODY.size() - 1, this.screenNum - 1))
            );
        this.imageEventText.clearAllDialogs();
        // draw 2 random choices from this.choices
        List<WildCardModifier> pool = new ArrayList<>(this.choices);
        int count = Math.min(2, pool.size());
        currentChoices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            DistributedRNG rng = new DistributedRNG();
            for (int j = 0; j < pool.size(); j++) {
                rng.addNumber(j, pool.get(j).getWeight());
            }
            currentChoices.add(pool.remove(rng.getDistributedRandomNumber(AbstractDungeon.miscRng)));
        }
        for (WildCardModifier option : currentChoices) {
            WildCard tempCard = new WildCard(Collections.singletonList(option));
            this.imageEventText.setDialogOption(OPTIONS[1] + getCardDescriptionForDisplay(tempCard) + OPTIONS[2] + hpLoss + OPTIONS[3]);
        }
        this.imageEventText.setDialogOption(OPTIONS[0], card);
    }

    private String getCardDescriptionForDisplay(AbstractCard card) {
        return card.description.stream()
                .map(d -> d.text)
                .collect(Collectors.joining(" NL "))
                .replace("!D!", card.damage + "")
                .replace("!B!", card.block + "")
                .replace("*", "#y");
    }
}
