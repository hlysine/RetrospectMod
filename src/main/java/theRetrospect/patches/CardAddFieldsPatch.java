package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theRetrospect.util.CardPlaySource;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class CardAddFieldsPatch {
    public static SpireField<CardPlaySource> playSource = new SpireField<>(() -> null);

    public static SpireField<List<AbstractGameAction>> followUpActions = new SpireField<>(ArrayList::new);
}

