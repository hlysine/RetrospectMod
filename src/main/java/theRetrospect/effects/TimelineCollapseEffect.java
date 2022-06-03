package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;

public class TimelineCollapseEffect extends AbstractGameEffect {

    private final AbstractFriendlyMonster minion;

    public TimelineCollapseEffect(AbstractFriendlyMonster minion) {
        this.minion = minion;
        this.duration = MathUtils.random(0.3F, 0.5F);
    }

    public void update() {
        AbstractDungeon.effectsQueue.add(new TimelineCollapseParticleEffect(minion));
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
