package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timetravel.RewindAction;
import theRetrospect.cards.AbstractBaseCard;

public class PsycheOut extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(PsycheOut.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public PsycheOut() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new RewindAction(this, timelineCount));
    }
}