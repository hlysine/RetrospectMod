package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineUtils;

public class ConcertedBlast extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(ConcertedBlast.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ConcertedBlast() {
        super(ID, TARGET);
    }

    private int calculateDamage() {
        int count = 0;
        for (TimelineMinion timeline : TimelineUtils.getTimelines(AbstractDungeon.player)) {
            if (timeline.isDead || timeline.isDeadOrEscaped()) continue;
            count += timeline.cards.size();
        }
        this.damage = this.baseDamage = count;
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateDamage();
        this.applyPowers();

        if (m != null) {
            addToBot(new VFXAction(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal)));
        }

        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
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

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.selfRetain = true;
        }
        super.upgrade();
    }
}
