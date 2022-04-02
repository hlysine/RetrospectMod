package theRetrospect.cards;

import basemod.abstracts.CustomCard;
import theRetrospect.characters.TheRetrospect;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractRetrospectCard extends CustomCard {

    public static final CardColor COLOR = TheRetrospect.Enums.RETROSPECT_CARD_VIOLET;

    public AbstractRetrospectCard(final String id,
                                  final String img,
                                  final int cost,
                                  final CardType type,
                                  final CardRarity rarity,
                                  final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, COLOR, rarity, target);
    }

    public boolean isReplayable() {
        return true;
    }
}