package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.cardActions.UnceasingEndTurnAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

public class UnceasingStrike extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(UnceasingStrike.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public UnceasingStrike() {
        super(ID, TARGET);

        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardUtils.getPlaySource(this) == CardPlaySource.TIMELINE)
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new UnceasingEndTurnAction());
    }
}
