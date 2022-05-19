package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.characters.TheRetrospect;

public abstract class AbstractRetrospectCard extends AbstractBaseCard {

    public static final CardColor COLOR = TheRetrospect.Enums.RETROSPECT_CARD_VIOLET;

    public boolean delusional = false;

    public AbstractRetrospectCard(final String id,
                                  final String img,
                                  final int cost,
                                  final CardType type,
                                  final CardRarity rarity,
                                  final CardTarget target) {

        super(id, img, cost, type, COLOR, rarity, target);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (delusional)
            addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }
}