package theRetrospect.actions.card;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.cards.old.attacks.ChargeRifle;
import theRetrospect.minions.TimelineMinion;

import java.util.List;
import java.util.stream.Collectors;

public class ChargeRifleAction extends AbstractGameAction {
    private final AbstractCard card;

    public ChargeRifleAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }


    public void update() {
        this.card.baseDamage += this.amount;
        this.card.applyPowers();

        List<TimelineMinion> timelines = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());

        for (TimelineMinion timeline : timelines) {
            for (AbstractCard c : timeline.cards) {
                if (c instanceof ChargeRifle) {
                    c.baseDamage += this.amount;
                    c.applyPowers();
                }
            }
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof ChargeRifle) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof ChargeRifle) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof ChargeRifle) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}
