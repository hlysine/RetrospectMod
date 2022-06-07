package theRetrospect.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import theRetrospect.effects.ShowCardToBePlayedEffect;

public class ShowCardToBePlayedAction extends AbstractGameAction {
    private final AbstractCard card;

    public ShowCardToBePlayedAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        addToTop(new VFXAction(null, new ShowCardToBePlayedEffect(card), Settings.FAST_MODE ? 0.5F : 1.5F, true));
        this.isDone = true;
    }
}
