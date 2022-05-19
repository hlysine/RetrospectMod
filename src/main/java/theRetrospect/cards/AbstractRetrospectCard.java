package theRetrospect.cards;

import theRetrospect.characters.TheRetrospect;

public abstract class AbstractRetrospectCard extends AbstractBaseCard {

    public static final CardColor COLOR = TheRetrospect.Enums.RETROSPECT_CARD_VIOLET;

    public AbstractRetrospectCard(final String id,
                                  final String img,
                                  final int cost,
                                  final CardType type,
                                  final CardRarity rarity,
                                  final CardTarget target) {

        super(id, img, cost, type, COLOR, rarity, target);
    }
}