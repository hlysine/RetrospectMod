package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.mechanics.card.CardPlaySource;
import theRetrospect.util.CardUtils;

public class AlternativeReality extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(AlternativeReality.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public AlternativeReality() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardUtils.getPlaySource(this) == CardPlaySource.TIMELINE) {
            addToBot(new GainBlockAction(p, p, block));
        } else {
            addToBot(new DrawCardAction(magicNumber));
        }
    }
}
