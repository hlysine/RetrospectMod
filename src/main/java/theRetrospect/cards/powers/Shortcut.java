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

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;

    private static final int BASE_COST = 1;

    public Shortcut() {
        super(ID, IMG, BASE_COST, TYPE, RARITY, TARGET);

        magicNumber = baseMagicNumber = ShortcutPower.UNIT_REDUCTION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShortcutPower(p, 1)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            this.initializeDescription();
        }
    }
}
