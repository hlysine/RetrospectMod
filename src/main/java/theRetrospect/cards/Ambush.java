package theRetrospect.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Ambush extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Ambush.class.getSimpleName());

    public static final String IMG = makeCardPath("ambush.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 2;
    private static final int DAMAGE = 5;
    private static final int BASE_CARD_COUNT = 6;
    private static final int UPGRADE_CARD_COUNT = -1;

    public Ambush() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BASE_CARD_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = p.drawPile.size();
        for (int i = 0; i < count; i += magicNumber) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_CARD_COUNT);
            initializeDescription();
        }
    }
}
