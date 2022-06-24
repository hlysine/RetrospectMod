package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.defect.NewThunderStrikeAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.TimelineUtils;

public class Anamnesis extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Anamnesis.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Anamnesis() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.magicNumber = this.baseMagicNumber = TimelineUtils.timelinesConstructedThisCombat.size();

        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new NewThunderStrikeAction(this));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        this.magicNumber = this.baseMagicNumber = TimelineUtils.timelinesConstructedThisCombat.size();

        if (this.baseMagicNumber > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (this.baseMagicNumber > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }
}
