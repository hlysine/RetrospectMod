package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.CardUtils;
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
        AbstractPlayer player = AbstractDungeon.player;
        TimelineMinion minion = new TimelineMinion(
                filterReplayableCards(AbstractDungeon.actionManager.cardsPlayedThisTurn),
                (int) (-Settings.WIDTH * 0.5), 0, healthBorrowed);

        MinionUtils.addMinion(player, minion);

        TimelineUtils.repositionTimelines(player);

        AbstractDungeon.actionManager.addToBottom(new NonTriggeringHealthChange(player, -healthBorrowed));

        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmpowerEffect(minion.drawX, minion.drawY)));

        this.isDone = true;
    }

    private List<AbstractCard> filterReplayableCards(List<AbstractCard> cards) {
        return cards.stream()
                .filter(CardUtils::isCardReplayable)
                .map(CardUtils::makeAdvancedCopy)
                .collect(Collectors.toList());
    }
}
