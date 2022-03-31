package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ReaperEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ForEachTimelineAction;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Harvest extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Harvest.class.getSimpleName());

    public static final String IMG = makeCardPath("Attack.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;

    public Harvest() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        baseDamage = BASE_DAMAGE;
        damage = baseDamage;
        isMultiDamage = true;

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

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }
}
