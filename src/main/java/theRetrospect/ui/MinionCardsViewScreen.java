package theRetrospect.ui;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import theRetrospect.RetrospectMod;

public class MinionCardsViewScreen extends AbstractPeekableViewScreen {
    public static final String ID = RetrospectMod.makeID(MinionCardsViewScreen.class.getSimpleName());

    @Override
    protected UIStrings getUIStrings() {
        return CardCrawlGame.languagePack.getUIString(ID);
    }

    @Override
    protected AbstractDungeon.CurrentScreen getScreen() {
        return UIManager.TIMELINE_CARDS_VIEW;
    }
}