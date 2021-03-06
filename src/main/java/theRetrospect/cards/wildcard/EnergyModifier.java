package theRetrospect.cards.wildcard;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import theRetrospect.RetrospectMod;

public class EnergyModifier extends WildCardModifier {
    public static final String ID = EnergyModifier.class.getSimpleName();
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(WildCard.ID);

    private int amount;

    public EnergyModifier(int amount) {
        this.amount = amount;
    }

    public EnergyModifier() {
        this.amount = 0;
    }

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.NONE;
    }

    @Override
    public void apply(AbstractCard card) {
        amount = RetrospectMod.getCardInfo(card.cardID).getBaseValue(ID);
    }

    @Override
    public void upgrade(AbstractCard card) {
        amount += RetrospectMod.getCardInfo(card.cardID).getUpgradeValue(ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + cardStrings.EXTENDED_DESCRIPTION[amount == 1 ? 7 : 8];
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
    }

    @Override
    public WildCardModifier makeCopy() {
        return new EnergyModifier(amount);
    }
}
