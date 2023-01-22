package theRetrospect.ui;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import theRetrospect.RetrospectMod;
import theRetrospect.util.CardUtils;

public class TurnCardsViewScreen extends AbstractViewScreen {
    public static final String ID = RetrospectMod.makeID(TurnCardsViewScreen.class.getSimpleName());

    @Override
    protected UIStrings getUIStrings() {
        return CardCrawlGame.languagePack.getUIString(ID);
    }

    @Override
    protected AbstractDungeon.CurrentScreen getScreen() {
        return UIManager.TURN_CARDS_VIEW;
    }

    public void open() {
        open(CardUtils.cardsManuallyPlayedThisTurn);
    }
}