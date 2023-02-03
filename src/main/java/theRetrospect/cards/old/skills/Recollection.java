package theRetrospect.cards.old.skills;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ChooseFromCardGroupAction;
import theRetrospect.cards.AbstractBaseCard;

public class Recollection extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Recollection.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Recollection() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String menuText;
        if (magicNumber > 1) {
            menuText = cardStrings.EXTENDED_DESCRIPTION[1] + magicNumber + cardStrings.EXTENDED_DESCRIPTION[2];
        } else {
            menuText = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        addToBot(new ChooseFromCardGroupAction(p.discardPile.group, menuText, magicNumber, false, false, card -> {
            AbstractCard newCard = card.makeStatEquivalentCopy();
            newCard.setCostForTurn(0);
            addToBot(new MakeTempCardInHandAction(newCard));
        }));
    }
}