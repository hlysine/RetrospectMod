package theRetrospect.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractRetrospectCard;
import theRetrospect.powers.ShortcutPower;

import static theRetrospect.RetrospectMod.makeCardPath;

public class Shortcut extends AbstractRetrospectCard {

    public static final String ID = RetrospectMod.makeID(Shortcut.class.getSimpleName());
    public static final String IMG = makeCardPath("shortcut.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int BASE_COST = 1;
    private static final int BASE_POWER = 5;
    private static final int UPGRADE_POWER = 2;


    public Shortcut() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = BASE_POWER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShortcutPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWER);
            this.initializeDescription();
        }
    }
}
