package theRetrospect.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractBaseCard extends CustomCard {

    public boolean delusional = false;

    public AbstractBaseCard(final String id,
                            final String img,
                            final int cost,
                            final CardType type,
                            final CardColor color,
                            final CardRarity rarity,
                            final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (delusional)
            addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }
}