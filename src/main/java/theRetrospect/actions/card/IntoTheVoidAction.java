package theRetrospect.actions.card;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.powers.IntoTheVoidPower;

public class IntoTheVoidAction extends AbstractGameAction {
    private final int hpLoss;

    public IntoTheVoidAction(int hpLoss) {
        this.hpLoss = hpLoss;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        addToTop(new LoseHPAction(player, player, hpLoss));
        if (!player.hasPower(IntoTheVoidPower.POWER_ID))
            addToTop(new ApplyPowerAction(player, player, new IntoTheVoidPower(player, 1)));
        this.isDone = true;
    }
}
