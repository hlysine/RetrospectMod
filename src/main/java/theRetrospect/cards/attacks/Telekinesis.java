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
import theRetrospect.cards.AbstractBaseCard;

public class Telekinesis extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Telekinesis.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Telekinesis() {
        super(ID, TARGET);

        DamageModifierManager.addModifier(this, new TelekinesisDamage());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
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
