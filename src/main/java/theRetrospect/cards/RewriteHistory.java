package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.ShuffleDiscardPileAction;

import static theRetrospect.RetrospectMod.makeCardPath;

public class RewriteHistory extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(RewriteHistory.class.getSimpleName());

    public static final String IMG = makeCardPath("Attack.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 2;
    private static final int BASE_MULTIPLIER = 2;
    private static final int UPGRADE_MULTIPLIER = 1;

    public RewriteHistory() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        baseMagicNumber = BASE_MULTIPLIER;
        magicNumber = baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        }
        addToBot(new WaitAction(0.8F));
        addToBot(new DamageAction(m, new DamageInfo(p, magicNumber * p.discardPile.size(), damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        addToBot(new ShuffleDiscardPileAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MULTIPLIER);
            initializeDescription();
        }
    }
}
