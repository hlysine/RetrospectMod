package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ForEachTimelineAction;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Concurrency extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Concurrency.class.getSimpleName());

    public static final String IMG = makeCardPath("concurrency.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 3;

    public Concurrency() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = BASE_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL);
        addToBot(new ForEachTimelineAction(timeline -> addToTop(new DamageAction(m, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true))));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}
