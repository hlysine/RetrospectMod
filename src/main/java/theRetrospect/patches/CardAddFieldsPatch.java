package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class CardAddFieldsPatch {
    public static SpireField<Boolean> isBeingReplayed = new SpireField<>(() -> false);

    public static SpireField<List<Runnable>> actionAfterUse = new SpireField<>(ArrayList::new);
}

