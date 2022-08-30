package theRetrospect.cards.wildcard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theRetrospect.RetrospectMod;

public class VulnerableModifier extends WildCardModifier {
    public static final String ID = VulnerableModifier.class.getSimpleName();
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(WildCard.ID);

    private int amount;

    public VulnerableModifier(int amount) {
        this.amount = amount;
    }

    public VulnerableModifier() {
        this.amount = 0;
    }

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.ENEMY;
    }

    @Override
    public float getWeight() {
        return 0.5f;
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
        return rawDescription + cardStrings.EXTENDED_DESCRIPTION[5].replace("!" + ID + "!", amount + "");
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, amount, false), amount));
    }

    @Override
    public WildCardModifier makeCopy() {
        return new VulnerableModifier(amount);
    }
}
