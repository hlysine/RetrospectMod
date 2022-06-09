package theRetrospect.actions.general;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.RetrospectMod;

public class SpecificCardToHandAction extends AbstractGameAction {

    private final AbstractCard card;
    private final CardGroup sourceGroup;

    public SpecificCardToHandAction(AbstractCard card, CardGroup sourceGroup) {
        this.card = card;
        this.sourceGroup = sourceGroup;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (sourceGroup.contains(this.card) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.hand.addToHand(this.card);
            this.card.stopGlowing();
            this.card.unhover();
            this.card.unfadeOut();
            this.card.setAngle(0.0F, true);
            this.card.lighten(false);
            this.card.drawScale = 0.12F;
            this.card.targetDrawScale = 0.75F;
            this.card.applyPowers();
            sourceGroup.removeCard(this.card);
        } else {
            RetrospectMod.logger.info("failed: " + sourceGroup.contains(this.card) + ", " + AbstractDungeon.player.hand.size());
        }

        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.glowCheck();
        this.isDone = true;
    }
}
