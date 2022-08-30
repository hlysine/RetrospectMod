package theRetrospect.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineTargeting;
import theRetrospect.util.TimelineUtils;

public abstract class TimelineTargetingCard extends AbstractBaseCard {

    public static final CardTarget TARGET = TimelineTargeting.TIMELINE;

    public static final String ID = RetrospectMod.makeID(TimelineTargetingCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public TimelineTargetingCard(final String id) {
        super(id, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TimelineMinion target = (TimelineMinion) TimelineTargeting.getTarget(this);
        if (target != null && (target.isDeadOrEscaped() || target.isDead)) target = null;
        if (target == null) target = TimelineUtils.getRandomTimeline(p, t -> t.cards.size() > 0);
        if (target == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[0], true));
            return;
        }

        useOnTarget(p, m, target);
    }

    protected abstract void useOnTarget(AbstractPlayer p, AbstractMonster m, TimelineMinion target);

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;

        if (TimelineUtils.getTimelines(p).size() == 0) {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }

        return true;
    }
}