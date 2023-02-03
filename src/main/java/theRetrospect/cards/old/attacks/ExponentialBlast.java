package theRetrospect.cards.old.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.util.CardUtils;

public class ExponentialBlast extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ExponentialBlast.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ExponentialBlast() {
        super(ID, TARGET);

        this.exhaust = true;
    }

    private int calculateDamage(int x) {
        int count = (int) Math.pow(2, x);
        this.damage = this.baseDamage = count;
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = CardUtils.calculateXCostEffect(this);

        effect -= this.magicNumber;

        if (effect > 0) {
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.getCurrentEnergy());
            }

            calculateDamage(effect);
            this.applyPowers();


            if (m != null) {
                addToBot(new VFXAction(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal)));
            }

            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
    }
}
