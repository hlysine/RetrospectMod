package theRetrospect.actions.universal;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theRetrospect.mechanics.timetravel.TimeTree;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.powers.UniversalStrengthPower;

import java.util.UUID;

public class UniversalStrengthAction extends AbstractUniversalAction {
    private final int amount;

    public UniversalStrengthAction(int amount) {
        super(null);
        this.amount = amount;
    }

    @Override
    protected void applyToCurrent(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amount), amount));
    }

    @Override
    protected void applyToNode(TimeTree.Node node, AbstractPlayer p, UUID monsterTarget) {
        node.applyRecursively(n -> {
            n.baseState.player.addPower(new StrengthPower(p, amount));
            n.midStates.forEach(midState -> midState.player.addPower(new StrengthPower(p, amount)));
        });
    }

    @Override
    protected Power getTimelinePower(TimelineMinion timeline, AbstractPlayer p, UUID monsterTarget) {
        return new UniversalStrengthPower(timeline, amount);
    }
}
