package theRetrospect.cards.statuses;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class Paradox extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Paradox.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public Paradox() {
        super(ID, TARGET);

        this.delusional = true;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            card.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
}
