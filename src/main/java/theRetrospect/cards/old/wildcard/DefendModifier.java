package theRetrospect.cards.old.wildcard;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import theRetrospect.RetrospectMod;

public class DefendModifier extends WildCardModifier {
    public static final String ID = DefendModifier.class.getSimpleName();
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(WildCard.ID);

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public AbstractCard.CardType getType() {
        return AbstractCard.CardType.SKILL;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.SELF;
    }

    @Override
    public float getWeight() {
        return 1f;
    }

    @Override
    public void apply(AbstractCard card) {
        card.block = card.baseBlock = RetrospectMod.getCardInfo(card.cardID).getBaseValue(ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, card.block));
    }

    @Override
    public WildCardModifier makeCopy() {
        return new DefendModifier();
    }
}
