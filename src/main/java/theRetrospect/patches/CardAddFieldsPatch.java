package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.util.CardPlaySource;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class CardAddFieldsPatch {
    /**
     * Record the source that played this card.
     */
    public static SpireField<CardPlaySource> playSource = new SpireField<>(() -> null);

    /**
     * A list of actions to be queued after the card is played.
     */
    public static SpireField<List<AbstractGameAction>> followUpActions = new SpireField<>(ArrayList::new);

    /**
     * If not null, return this card to a minion (timeline) after playing instead of purging it.
     */
    public static SpireField<AbstractMinionWithCards> returnToMinion = new SpireField<>(() -> null);
}

