package theRetrospect.cards;

import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.mechanics.timetravel.TimeManager;
import theRetrospect.mechanics.timetravel.TimeTravelTargeting;
import theRetrospect.mechanics.timetravel.TimeTree;
import theRetrospect.powers.TimeLinkPower;
import theRetrospect.util.MonsterUtils;

public abstract class TimeTravelCard extends AbstractBaseCard {

    public static final CardTarget TARGET = TimeTravelTargeting.TIME_TRAVEL;

    public static final String ID = RetrospectMod.makeID(TimeTravelCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public TimeTravelCard(final String id) {
        super(id, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = TimeTravelTargeting.getTarget(this);
        if (target != null && target.isDeadOrEscaped()) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, cardStrings.EXTENDED_DESCRIPTION[0], true));
            return;
        }
        if (target == null) target = AbstractDungeon.player;

        useOnTarget(p, m, target);
        CustomTargeting.setCardTarget(this, null);
    }

    protected abstract int getTravelDistance();

    protected abstract void useOnTarget(AbstractPlayer p, AbstractMonster m, AbstractCreature target);

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        AbstractCreature target = TimeTravelTargeting.getTarget(this);
        if (target == p || target == null) {
            return true;
        }
        AbstractMonster monster = (AbstractMonster) target;

        if (monster.isDeadOrEscaped()) {
            return cantUse(cardStrings.EXTENDED_DESCRIPTION[0]);
        }

        AbstractPower timeLink = monster.getPower(TimeLinkPower.POWER_ID);
        int travelDistance = getTravelDistance();
        if (timeLink == null) {
            return cantUse(cardStrings.EXTENDED_DESCRIPTION[1]);
        } else if (timeLink.amount < travelDistance) {
            StringBuilder sb = new StringBuilder();
            sb.append(cardStrings.EXTENDED_DESCRIPTION[2]);
            sb.append(travelDistance);
            if (travelDistance <= 1) {
                sb.append(cardStrings.EXTENDED_DESCRIPTION[3]);
            } else {
                sb.append(cardStrings.EXTENDED_DESCRIPTION[4]);
            }
            return cantUse(sb.toString());
        } else {
            TimeTree stateTree = TimeManager.timeTree;
            TimeTree.Node destination = stateTree.getNodeForRound(stateTree.getActiveNode(), stateTree.getActiveRound() - travelDistance);
            if (destination == null) destination = stateTree.getRoot(stateTree.getActiveNode());
            if (destination.baseState.monsters.monsters.stream().noneMatch(m1 -> MonsterUtils.isSameMonster(m1, monster))) {
                return cantUse(cardStrings.EXTENDED_DESCRIPTION[5]);
            }
        }

        return true;
    }

    private boolean cantUse(String cantUseMessage) {
        this.cantUseMessage = cantUseMessage;
        addToBot(new RunnableAction(() -> AbstractDungeon.player.hand.refreshHandLayout()));
        CustomTargeting.setCardTarget(this, null);
        return false;
    }
}