package theRetrospect.cards.old.wildcard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import theRetrospect.RetrospectMod;

public class AttackModifier extends WildCardModifier {
    public static final String ID = AttackModifier.class.getSimpleName();
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(WildCard.ID);

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public AbstractCard.CardType getType() {
        return AbstractCard.CardType.ATTACK;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.ENEMY;
    }

    @Override
    public float getWeight() {
        return 1f;
    }

    @Override
    public void apply(AbstractCard card) {
        card.damage = card.baseDamage = RetrospectMod.getCardInfo(card.cardID).getBaseValue(ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                target,
                new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
    }

    @Override
    public WildCardModifier makeCopy() {
        return new AttackModifier();
    }
}
