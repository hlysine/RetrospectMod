package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.universal.UniversalStrengthAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.powers.TimeLinkPower;

public class Strike extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Strike.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike() {
        super(ID, TARGET);

        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p, new TimeLinkPower(m, damage))); // todo: debug only
        addToBot(new UniversalStrengthAction(1));
    }
}
