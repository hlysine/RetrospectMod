package theRetrospect.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UIManager {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen TURN_CARDS_VIEW;

    public static TurnCardsPanel turnCardsPanel;
    public static TurnCardsViewScreen turnCardsViewScreen;

    public static void initialize() {
        turnCardsPanel = new TurnCardsPanel();
        turnCardsViewScreen = new TurnCardsViewScreen();
    }
}
