package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ReaperEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ForEachTimelineAction;
import theRetrospect.cards.AbstractBaseCard;

public class Harvest extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Harvest.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Harvest() {
        super(ID, TARGET);

        this.isMultiDamage = true;

        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ForEachTimelineAction(
                timeline -> {
                    addToBot(new VFXAction(new ReaperEffect()));
                    addToBot(new VampireDamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
                }
        ));
    }
}
