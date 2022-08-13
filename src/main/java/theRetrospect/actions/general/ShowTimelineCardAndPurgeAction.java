package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.effects.PurgeTimelineCardEffect;

public class ShowTimelineCardAndPurgeAction extends AbstractGameAction {
    private final AbstractCard card;
    private static final float PURGE_DURATION = 0.2F;

    public ShowTimelineCardAndPurgeAction(AbstractCard card) {
        setValues(AbstractDungeon.player, null, 1);
        this.card = card;
        this.duration = PURGE_DURATION;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }


    public void update() {
        if (this.duration == 0.2F) {
            AbstractDungeon.effectList.add(new PurgeTimelineCardEffect(this.card));
            if (AbstractDungeon.player.limbo.contains(this.card)) {
                AbstractDungeon.player.limbo.removeCard(this.card);
            }
            AbstractDungeon.player.cardInUse = null;
        }
        tickDuration();
    }
}
