package theRetrospect.cards.old.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timeline.ForEachTimelineAction;
import theRetrospect.cards.AbstractBaseCard;

public class Concurrency extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Concurrency.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Concurrency() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL);
        addToBot(new ForEachTimelineAction(timeline -> addToTop(new DamageAction(m, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true))));
    }
}
