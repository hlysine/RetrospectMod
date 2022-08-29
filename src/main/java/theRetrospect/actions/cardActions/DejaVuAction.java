package theRetrospect.actions.cardActions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hlysine.friendlymonsters.utils.MinionUtils;
import theRetrospect.cards.skills.DejaVu;
import theRetrospect.minions.TimelineMinion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DejaVuAction extends AbstractGameAction {
    private final AbstractCard card;

    public DejaVuAction(AbstractCard card) {
        this.card = card;
    }


    public void update() {
        List<UUID> upgradedIds = new ArrayList<>();

        addToBot(new IncreaseMiscAction(card.uuid, card.misc, card.magicNumber));
        upgradedIds.add(card.uuid);

        List<TimelineMinion> timelines = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream()
                .filter(m -> m instanceof TimelineMinion)
                .map(m -> (TimelineMinion) m)
                .collect(Collectors.toList());

        for (TimelineMinion timeline : timelines) {
            for (AbstractCard c : timeline.cards) {
                processCard(c, upgradedIds);
            }
        }

        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            processCard(c, upgradedIds);
        }

        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            processCard(c, upgradedIds);
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            processCard(c, upgradedIds);
        }

        for (AbstractCard c : AbstractDungeon.player.limbo.group) {
            processCard(c, upgradedIds);
        }

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            processCard(c, upgradedIds);
        }

        this.isDone = true;
    }

    private void processCard(AbstractCard c, List<UUID> upgradedIds) {
        if (c instanceof DejaVu) {
            if (upgradedIds.stream().noneMatch(id -> id.equals(c.uuid))) {
                addToBot(new IncreaseMiscAction(c.uuid, c.misc, card.magicNumber));
                upgradedIds.add(c.uuid);
            }
        }
    }
}
