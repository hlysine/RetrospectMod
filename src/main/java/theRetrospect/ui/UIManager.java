package theRetrospect.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UIManager {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen TURN_CARDS_VIEW;
    @SpireEnum
    public static AbstractDungeon.CurrentScreen TIMELINE_CARDS_VIEW;

    public static TurnCardsPanel turnCardsPanel;
    public static TurnCardsViewScreen turnCardsViewScreen;

    public static MinionCardsViewScreen minionCardsViewScreen;

    public static void initialize() {
        turnCardsPanel = new TurnCardsPanel();
        turnCardsViewScreen = new TurnCardsViewScreen();
        minionCardsViewScreen = new MinionCardsViewScreen();
    }
}
