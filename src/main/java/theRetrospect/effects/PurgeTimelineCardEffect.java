package theRetrospect.effects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.RetrospectMod;

public class PurgeTimelineCardEffect extends AbstractGameEffect {
    private final AbstractCard card;
    private static final float DUR = 1.0F;

    public PurgeTimelineCardEffect(AbstractCard card) {
        this.duration = DUR;
        this.card = card;
    }

    public void update() {
        if (this.duration == 1.0F) {
            CardCrawlGame.sound.play("CARD_EXHAUST", 1F);
            for (int i = 0; i < 90; i++) {
                AbstractDungeon.effectsQueue.add(new TimelinePurgeParticleEffect(this.card.current_x, this.card.current_y));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (!this.card.fadingOut && this.duration < 0.7F &&
                !AbstractDungeon.player.hand.contains(this.card)) {
            this.card.fadingOut = true;
            this.card.superFlash(RetrospectMod.RETROSPECT_VIOLET.cpy());
            this.card.stopGlowing();
            this.card.lighten(false);
        }
        this.card.update();

        if (this.duration < 0.0F) {
            this.isDone = true;
            this.card.resetAttributes();
        }
    }


    public void render(SpriteBatch sb) {
        this.card.render(sb);
    }

    public void dispose() {
    }
}
