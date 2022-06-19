package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.STSCardInfo.CardInfo;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Strike extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Strike.class.getSimpleName());

    private static final CardInfo INFO = RetrospectMod.getCardInfo(ID);
    public static final String IMG = makeCardPath(INFO.getImage());

    private static final CardRarity RARITY = INFO.getRarity();
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = INFO.getType();

    public Strike() {
        super(ID, IMG, INFO.getBaseCost(), TYPE, RARITY, TARGET);

        INFO.setBaseValues(this);

        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            INFO.upgradeValues(this);
            initializeDescription();
        }
    }
}
