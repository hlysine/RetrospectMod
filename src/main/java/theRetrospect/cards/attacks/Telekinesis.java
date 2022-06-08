package theRetrospect.cards.attacks;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Telekinesis extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Telekinesis.class.getSimpleName());

    public static final String IMG = makeCardPath("telekinesis.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 4;

    public Telekinesis() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = BASE_DAMAGE;
        DamageModifierManager.addModifier(this, new TelekinesisDamage());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }

    public static class TelekinesisDamage extends AbstractDamageModifier {
        @Override
        public AbstractDamageModifier makeCopy() {
            return new TelekinesisDamage();
        }

        @Override
        public boolean isInherent() {
            return true;
        }

        @Override
        public boolean ignoresBlock(AbstractCreature target) {
            return true;
        }
    }
}
