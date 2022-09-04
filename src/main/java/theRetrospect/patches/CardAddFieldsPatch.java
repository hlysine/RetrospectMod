package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.util.CardFollowUpAction;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardReturnInfo;

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
     * Contains information about the minion that this card should return to after being played.
     */
    public static final SpireField<CardReturnInfo> returnInfo = new SpireField<>(CardReturnInfo::new);

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

