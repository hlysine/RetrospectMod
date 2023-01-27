package theRetrospect.actions.card;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theRetrospect.powers.TheBestDefensePower;

public class TheBestDefenseAction extends AbstractGameAction {
    private final int blockGain;

    public TheBestDefenseAction(int blockGain) {
        this.blockGain = blockGain;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (blockGain > 0) {
            addToBot(new GainBlockAction(player, blockGain));
        }
        if (!player.hasPower(TheBestDefensePower.POWER_ID))
            addToTop(new ApplyPowerAction(player, player, new TheBestDefensePower(player, 1)));
        this.isDone = true;
    }
}
