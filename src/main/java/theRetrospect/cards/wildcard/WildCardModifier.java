package theRetrospect.cards.wildcard;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class WildCardModifier extends AbstractCardModifier {
    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    public abstract String getKey();

    public abstract AbstractCard.CardTarget getTarget();

    public abstract void apply(AbstractCard card);

    public abstract void upgrade(AbstractCard card);

    @Override
    public abstract WildCardModifier makeCopy();
}
