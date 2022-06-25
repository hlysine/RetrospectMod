package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;

public class Enigma extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Enigma.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Enigma() {
        super(ID, TARGET);
    }

    private int calculateDamage() {
        int count = (int) AbstractDungeon.player.exhaustPile.group.stream().filter(c -> c instanceof Paradox).count();
        this.damage = this.baseDamage = this.magicNumber * count;
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = calculateDamage();
        this.applyPowers();

        if (m != null) {
            addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, count), 0.2F));
        }

        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void applyPowers() {
        int count = calculateDamage();
        super.applyPowers();

        if (count > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int count = calculateDamage();
        super.calculateCardDamage(mo);
        if (count > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }
}
