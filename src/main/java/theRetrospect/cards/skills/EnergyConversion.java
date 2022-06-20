package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

public class EnergyConversion extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(EnergyConversion.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public EnergyConversion() {
        super(ID, TARGET);

        this.cardsToPreview = new Miracle();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = CardUtils.calculateXCostEffect(this);

        effect += this.magicNumber;

        if (effect > 0) {
            addToTop(new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy(), effect));

            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }
}