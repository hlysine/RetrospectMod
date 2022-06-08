package theRetrospect;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.eventUtil.AddEventParams;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.characters.TheRetrospect;
import theRetrospect.events.MysteriousProphetEvent;
import theRetrospect.potions.TimelinePotion;
import theRetrospect.relics.AbstractBaseRelic;
import theRetrospect.util.IDCheckDontTouchPls;
import theRetrospect.util.TextureLoader;
import theRetrospect.util.TimelineTargeting;
import theRetrospect.variables.TimelineCountVariable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SpireInitializer
public class RetrospectMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {

    public static final Logger logger = LogManager.getLogger(RetrospectMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MOD_NAME = "The Retrospect";
    private static final String AUTHOR = "Lysine";
    private static final String DESCRIPTION = "A character mod for the Retrospect.";

    // Character Color
    public static final Color RETROSPECT_VIOLET = CardHelper.getColor(102, 27, 255);

    // Card backgrounds - The actual rectangular card.
    private static final String CARD_BG_ATTACK = "theRetrospectResources/images/512/bg_attack_retrospect_purple.png";
    private static final String CARD_BG_SKILL = "theRetrospectResources/images/512/bg_skill_retrospect_purple.png";
    private static final String CARD_BG_POWER = "theRetrospectResources/images/512/bg_power_retrospect_purple.png";

    private static final String ENERGY_ORB_SMALL = "theRetrospectResources/images/512/card_text_energy_icon_small.png";
    private static final String ENERGY_ORB_CARD_CORNER = "theRetrospectResources/images/512/card_corner_energy.png";
    private static final String ENERGY_ORB_LARGE = "theRetrospectResources/images/1024/card_text_energy_icon_large.png";

    private static final String CARD_BG_ATTACK_LARGE = "theRetrospectResources/images/1024/bg_attack_retrospect_purple.png";
    private static final String CARD_BG_SKILL_LARGE = "theRetrospectResources/images/1024/bg_skill_retrospect_purple.png";
    private static final String CARD_BG_POWER_LARGE = "theRetrospectResources/images/1024/bg_power_retrospect_purple.png";

    // Character assets
    private static final String THE_RETROSPECT_BUTTON = "theRetrospectResources/images/charSelect/RetrospectCharacterButton.png";
    private static final String THE_RETROSPECT_PORTRAIT = "theRetrospectResources/images/charSelect/RetrospectCharacterPortraitBG.png";
    public static final String THE_RETROSPECT_SHOULDER_1 = "theRetrospectResources/images/char/retrospectCharacter/shoulder.png";
    public static final String THE_RETROSPECT_SHOULDER_2 = "theRetrospectResources/images/char/retrospectCharacter/shoulder2.png";
    public static final String THE_RETROSPECT_CORPSE = "theRetrospectResources/images/char/retrospectCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu.
    public static final String BADGE_IMAGE = "theRetrospectResources/images/Badge.png";

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public RetrospectMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        setModID("theRetrospect");

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheRetrospect.Enums.RETROSPECT_CARD_VIOLET.toString());

        BaseMod.addColor(TheRetrospect.Enums.RETROSPECT_CARD_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                RETROSPECT_VIOLET,
                CARD_BG_ATTACK,
                CARD_BG_SKILL,
                CARD_BG_POWER,
                ENERGY_ORB_CARD_CORNER,
                CARD_BG_ATTACK_LARGE,
                CARD_BG_SKILL_LARGE,
                CARD_BG_POWER_LARGE,
                ENERGY_ORB_LARGE,
                ENERGY_ORB_SMALL);

        logger.info("Done creating the color");
    }

    public static void setModID(String ID) {
        Gson coolG = new Gson();
        InputStream in = RetrospectMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        assert in != null;
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
        logger.info("Success! ID is " + modID);
    }

    public static String getModID() {
        return modID;
    }

    private static void pathCheck() {
        Gson coolG = new Gson();
        InputStream in = RetrospectMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        assert in != null;
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = RetrospectMod.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources");
            }
        }
    }

    public static void initialize() {
        logger.info("========================= Initializing Retrospect Mod. =========================");
        @SuppressWarnings("unused") RetrospectMod retrospectMod = new RetrospectMod();
        logger.info("========================= /Retrospect Mod Initialized./ =========================");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheRetrospect.Enums.THE_RETROSPECT.toString());

        BaseMod.addCharacter(new TheRetrospect("the Retrospect", TheRetrospect.Enums.THE_RETROSPECT),
                THE_RETROSPECT_BUTTON, THE_RETROSPECT_PORTRAIT, TheRetrospect.Enums.THE_RETROSPECT);

        receiveEditPotions();
        logger.info("Added " + TheRetrospect.Enums.THE_RETROSPECT.toString());
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, null);

        CustomTargeting.registerCustomTargeting(TimelineTargeting.TIMELINE, new TimelineTargeting());

        logger.info("Done loading badge Image and mod options");

        AddEventParams eventParams = new AddEventParams.Builder(MysteriousProphetEvent.ID, MysteriousProphetEvent.class)
                .playerClass(TheRetrospect.Enums.THE_RETROSPECT)
                .create();

        BaseMod.addEvent(eventParams);
    }

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(TimelinePotion.class,
                TimelinePotion.LIQUID_COLOR,
                TimelinePotion.HYBRID_COLOR,
                TimelinePotion.SPOTS_COLOR,
                TimelinePotion.POTION_ID);

        logger.info("Done editing potions");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        new AutoAdd("RetrospectMod")
                .packageFilter(AbstractBaseRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    if (!info.ignore) {
                        BaseMod.addRelicToCustomPool(relic, TheRetrospect.Enums.RETROSPECT_CARD_VIOLET);
                        if (info.seen) {
                            UnlockTracker.markRelicAsSeen(relic.relicId);
                        }
                    }
                });

        logger.info("Done adding relics!");
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        pathCheck();
        BaseMod.addDynamicVariable(new TimelineCountVariable());

        logger.info("Adding cards");

        new AutoAdd("RetrospectMod")
                .packageFilter(AbstractRetrospectCard.class)
                .setDefaultSeen(false)
                .cards();

        logger.info("Done adding cards!");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Card-Strings.json");

        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Power-Strings.json");

        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Relic-Strings.json");

        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Event-Strings.json");

        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Potion-Strings.json");

        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Character-Strings.json");

        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Orb-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Ui-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                getModID() + "Resources/localization/eng/RetrospectMod-Tutorial-Strings.json");

        logger.info("Done editing strings");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/RetrospectMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
