package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.util.CardPlaySource;

import static theRetrospect.RetrospectMod.makeCardPath;

public class SleightOfHand extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(SleightOfHand.class.getSimpleName());

    public static final String IMG = makeCardPath("sleight_of_hand.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 4;

    public SleightOfHand() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);

        damage = baseDamage = BASE_DAMAGE;
        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ReplayLastCardsAction(card -> card != this, 1, CardPlaySource.CARD));
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