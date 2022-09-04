package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TimelineUtils;

public class TimeLeap extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(TimeLeap.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TimeLeap() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new GainBlockAction(p, p, block));
        TimelineMinion timeline = TimelineUtils.getRandomTimeline(p);
        if (timeline != null) {
            CardUtils.setReturnInfo(this, timeline, true);
        }
    }
}
