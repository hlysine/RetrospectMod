package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.FtueTip;

import java.util.Iterator;

public class ShuffleDiscardPileAction extends AbstractGameAction {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Shuffle Tip");
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    private boolean shuffled = false;
    private boolean vfxDone = false;
    private int count = 0;


    public ShuffleDiscardPileAction() {
        setValues(null, null, 0);
        this.actionType = AbstractGameAction.ActionType.SHUFFLE;

        if (!TipTracker.tips.get("SHUFFLE_TIP")) {
            AbstractDungeon.ftue = new FtueTip(LABEL[0], MSG[0], Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, FtueTip.TipType.SHUFFLE);

            TipTracker.neverShowAgain("SHUFFLE_TIP");
        }
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onShuffle();
        }
    }


    public void update() {
        if (!this.shuffled) {
            this.shuffled = true;
            AbstractDungeon.player.discardPile.shuffle(AbstractDungeon.shuffleRng);
        }


        if (!this.vfxDone) {
            Iterator<AbstractCard> c = AbstractDungeon.player.discardPile.group.iterator();
            if (c.hasNext()) {
                this.count++;
                AbstractCard e = c.next();
                c.remove();
                (AbstractDungeon.getCurrRoom()).souls.shuffle(e, this.count >= 11);
                return;
            }
            this.vfxDone = true;
        }

        this.isDone = true;
    }
}
