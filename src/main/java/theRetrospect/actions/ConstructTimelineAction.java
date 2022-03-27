package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.cards.Divert;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.MinionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructTimelineAction extends AbstractGameAction {

    private final int healthBorrowed;

    public ConstructTimelineAction(int healthBorrowed) {
        this.healthBorrowed = healthBorrowed;
    }

    @Override
    public void update() {
        AbstractFriendlyMonster minion = new TimelineMinion(
                filterReplayableCards(AbstractDungeon.actionManager.cardsPlayedThisTurn),
                (int) (Math.random() * 200 + -700), (int) (Math.random() * 200 - 100), healthBorrowed);
        MinionUtils.addMinion(AbstractDungeon.player, minion);

        AbstractDungeon.actionManager.addToBottom(new NonTriggeringHealthChange(AbstractDungeon.player, -healthBorrowed));

        this.isDone = true;
    }

    private List<AbstractCard> filterReplayableCards(List<AbstractCard> cards) {
        return cards.stream()
                .filter(c -> !(c instanceof AbstractRetrospectCard) || ((AbstractRetrospectCard) c).isReplayable())
                .map(AbstractCard::makeStatEquivalentCopy)
                .collect(Collectors.toList());
    }
}
