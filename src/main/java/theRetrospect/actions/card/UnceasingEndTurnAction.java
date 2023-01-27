package theRetrospect.actions.card;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import theRetrospect.effects.UnceasingEndTurnEffect;

public class UnceasingEndTurnAction extends AbstractGameAction {
    @Override
    public void update() {
        addToTop(new PressEndTurnButtonAction());

        CardCrawlGame.sound.playAV("POWER_TIME_WARP", 1.2f, 0.5f);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
        AbstractDungeon.topLevelEffectsQueue.add(new UnceasingEndTurnEffect());

        this.isDone = true;
    }
}
