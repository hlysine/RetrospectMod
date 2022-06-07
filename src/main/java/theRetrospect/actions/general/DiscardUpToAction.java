package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class DiscardUpToAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private final AbstractPlayer p;
    private final boolean canPickZero;
    public static int numDiscarded;
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    public DiscardUpToAction(AbstractCreature target, AbstractCreature source, int amount, boolean canPickZero) {
        this.p = (AbstractPlayer) target;
        this.canPickZero = canPickZero;
        setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.DISCARD;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            int trueAmount = this.amount < 0 ? 99 : this.amount;
            numDiscarded = this.amount;
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], trueAmount, true, canPickZero, false, false, true);
            AbstractDungeon.player.hand.applyPowers();
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}