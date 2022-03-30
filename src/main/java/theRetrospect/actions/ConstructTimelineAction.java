package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;
import theRetrospect.util.TimelineUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructTimelineAction extends AbstractGameAction {

    private final int healthBorrowed;

    public ConstructTimelineAction(int healthBorrowed) {
        this.healthBorrowed = healthBorrowed;
    }

    @Override
    public void update() {
        TimelineMinion minion = new TimelineMinion(
                filterReplayableCards(AbstractDungeon.actionManager.cardsPlayedThisTurn),
                (int) (-Settings.WIDTH * 0.5), 0, healthBorrowed);

        MinionUtils.addMinion(AbstractDungeon.player, minion);

        TimelineUtils.repositionTimelines(AbstractDungeon.player);

        AbstractDungeon.actionManager.addToBottom(new NonTriggeringHealthChange(AbstractDungeon.player, -healthBorrowed));

        this.isDone = true;
    }

    private List<AbstractCard> filterReplayableCards(List<AbstractCard> cards) {
        return cards.stream()
                .filter(c -> !(c instanceof AbstractRetrospectCard) || ((AbstractRetrospectCard) c).isReplayable())
                .map(card -> {
                    AbstractCard newCard = card.makeStatEquivalentCopy();
                    newCard.current_x = card.current_x;
                    newCard.target_x = newCard.current_x;
                    newCard.current_y = card.current_y;
                    newCard.target_y = newCard.current_y;
                    return newCard;
                })
                .collect(Collectors.toList());
    }
}
