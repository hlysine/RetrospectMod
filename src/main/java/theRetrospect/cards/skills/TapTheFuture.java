package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Empty;

public class TapTheFuture extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(TapTheFuture.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public TapTheFuture() {
        super(ID, TARGET);

        this.cardsToPreview = new Empty();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cards = info.getBaseValue("cards");
        addToBot(new GainEnergyAction(this.magicNumber));
        addToBot(new MakeTempCardInDrawPileAction(this.cardsToPreview.makeStatEquivalentCopy(), cards, true, true));
    }
}