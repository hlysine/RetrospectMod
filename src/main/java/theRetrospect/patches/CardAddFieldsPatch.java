package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.util.CardFollowUpAction;
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
    public static final SpireField<CardPlaySource> playSource = new SpireField<>(() -> null);

    /**
     * A list of actions to be queued after the card is played.
     */
    public static final SpireField<List<CardFollowUpAction>> followUpActions = new SpireField<>(ArrayList::new);

    /**
     * If not null, return this card to a minion (timeline) after playing instead of purging it.
     */
    public static final SpireField<AbstractMinionWithCards> returnToMinion = new SpireField<>(() -> null);

    /**
     * Whether this card causes the player's turn to end.
     * <p>
     * It can be due to the card's effect (such as Conclude), or due to powers such as Time Warp.
     */
    public static final SpireField<Boolean> turnEnding = new SpireField<>(() -> false);

    /**
     * Whether this card is bottled by Bottled Singularity.
     */
    public static final SpireField<Boolean> isInBottledSingularity = new SpireField<>(() -> false);
}

