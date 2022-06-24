package theRetrospect.actions.cardActions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import theRetrospect.cards.skills.SelfCorrection;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.CardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelfCorrectionAction extends AbstractGameAction {

    private final TimelineMinion timeline;
    private final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(SelfCorrection.ID);

    public SelfCorrectionAction(TimelineMinion timeline) {
        this.timeline = timeline;
    }

    @Override
    public void update() {
        if (timeline.cards.size() <= 0) {
            this.isDone = true;
            return;
        }

        Map<AbstractCard, AbstractCard> cardMap = new HashMap<>();
        ArrayList<AbstractCard> cardsToSelect = new ArrayList<>();
        for (AbstractCard card : timeline.cards) {
            AbstractCard copy = CardUtils.makeAdvancedCopy(card, true);
            cardMap.put(card, copy);
            cardsToSelect.add(copy);
        }

        addToTop(new SelectCardsAction(
                cardsToSelect,
                Integer.MAX_VALUE,
                cardStrings.EXTENDED_DESCRIPTION[0],
                true,
                c -> true,
                cards -> {
                    timeline.cards.removeIf(c -> cards.contains(cardMap.get(c)));
                    timeline.triggerCardsChange();
                }
        ));

        this.isDone = true;
    }
}
