package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.mechanics.timetravel.TimeManager;

public class RiskyStrike extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(RiskyStrike.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public RiskyStrike() {
        super(ID, TARGET);

        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int roll = TimeManager.unstableRng.random(magicNumber - 1);
        if (roll == 0) {
            addToBot(new HealAction(m, p, damage));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }
}