package theRetrospect.cards.old.statuses;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;

public class Paradox extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(Paradox.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.NONE;

    public Paradox() {
        super(ID, TARGET);

        this.isEthereal = true;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof Paradox) {
                    c.beginGlowing();
                    c.flash(RetrospectMod.RETROSPECT_COLOR.cpy());
                }
            }
            card.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Paradox) {
                c.stopGlowing();
            }
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        this.returnToHand = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }
}