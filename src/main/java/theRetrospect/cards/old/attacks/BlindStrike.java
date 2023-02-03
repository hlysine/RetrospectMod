package theRetrospect.cards.old.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class BlindStrike extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(BlindStrike.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BlindStrike() {
        super(ID, TARGET);

        cardsToPreview = new Dazed();

        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToTop(new MakeTempCardInDrawPileAction(new Dazed(), magicNumber, true, true));
    }
}
