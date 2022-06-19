package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.STSCardInfo.CardInfo;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Defend extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Defend.class.getSimpleName());

    private static final CardInfo INFO = RetrospectMod.getCardInfo(ID);
    public static final String IMG = makeCardPath(INFO.getImage());

    private static final CardRarity RARITY = INFO.getRarity();
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = INFO.getType();

    public Defend() {
        super(ID, IMG, INFO.getBaseCost(), TYPE, RARITY, TARGET);

        INFO.setBaseValues(this);

        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
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
