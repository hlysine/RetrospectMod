package theRetrospect.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.ReplayLastCardsAction;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.cards.statuses.Paradox;
import theRetrospect.mechanics.cardAttributes.CardPlaySource;
import theRetrospect.util.CardUtils;

public class SleightOfHand extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(SleightOfHand.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SleightOfHand() {
        super(ID, TARGET);

        paradoxical = true;
        cardsToPreview = new Paradox();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        CardUtils.addFollowUpActionToBottom(this, new ReplayLastCardsAction(
                this,
                null,
                1,
                CardPlaySource.CARD,
                this.current_x,
                this.current_y
        ), true, 0);
    }
}
