package theRetrospect.cards.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.timelineActions.ConstructMultipleTimelineAction;
import theRetrospect.cards.AbstractBaseCard;

public class LightspeedLeap extends AbstractBaseCard {

    public static final String ID = RetrospectMod.makeID(LightspeedLeap.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;

    public LightspeedLeap() {
        super(ID, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConstructMultipleTimelineAction(this, timelineCount));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }

        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < magicNumber) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= magicNumber) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}