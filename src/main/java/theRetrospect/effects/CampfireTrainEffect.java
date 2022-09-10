package theRetrospect.effects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import theRetrospect.relics.AnkleWeights;

public class CampfireTrainEffect extends AbstractGameEffect {
    private boolean hasTrained = false;
    private final Color screenColor = AbstractDungeon.fadeColor.cpy();

    public CampfireTrainEffect() {
        this.duration = 2.0F;
        this.screenColor.a = 0.0F;
        ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
    }


    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        updateBlackScreenColor();

        if (this.duration < 1.0F && !this.hasTrained) {
            this.hasTrained = true;
            if (AbstractDungeon.player.hasRelic(AnkleWeights.ID)) {
                AbstractRelic relic = AbstractDungeon.player.getRelic(AnkleWeights.ID);
                relic.flash();
                relic.counter++;
                CardCrawlGame.sound.play("ATTACK_HEAVY");
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, true);
                CardCrawlGame.metricData.addCampfireChoiceData("TRAIN", Integer.toString(relic.counter));
            }
            AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(0.8F, 0.6F, 0.1F, 0.0F)));
        }


        if (this.duration < 0.0F) {
            this.isDone = true;
            ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
            (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.5F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.5F) * 2.0F);

        } else if (this.duration < 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        } else {
            this.screenColor.a = 1.0F;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
    }

    public void dispose() {
    }
}
