package theRetrospect.cards.old.wildcard;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class WildCardModifier extends AbstractCardModifier {
    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    public abstract String getKey();

    public abstract AbstractCard.CardType getType();

    public abstract AbstractCard.CardTarget getTarget();

    public abstract float getWeight();

    public abstract void apply(AbstractCard card);

    @Override
    public abstract WildCardModifier makeCopy();
}
