package theRetrospect.minions;

import basemod.BaseMod;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpineAnimation;
import basemod.interfaces.ModelRenderSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * Reimplements {@link AbstractMonster} rendering logic to allow for Spriter/Spine animations.
 */
public class AbstractAnimatedFriendlyMonster extends AbstractFriendlyMonster implements ModelRenderSubscriber {

    private final static MethodHandle renderNameHandle;

    static {
        MethodHandle tmpHandle;
        try {
            Method renderMethod = AbstractMonster.class.getDeclaredMethod("renderName", SpriteBatch.class);
            renderMethod.setAccessible(true);
            tmpHandle = MethodHandles.publicLookup().unreflect(renderMethod);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            tmpHandle = null;
        }
        renderNameHandle = tmpHandle;
    }

    public AbstractAnimation animation;
    public boolean renderCorpse = false;
    public float scale = 1.0f;
    public Texture corpseImg = null;

    public AnimationStateData getStateData() {
        return this.stateData;
    }

    public AbstractAnimatedFriendlyMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }

    protected void loadAnimation(AbstractAnimation animation) {
        this.animation = animation;
        if (animation instanceof SpineAnimation) {
            SpineAnimation spine = (SpineAnimation) animation;
            this.loadAnimation(spine.atlasUrl, spine.skeletonUrl, spine.scale);
        }

        if (animation.type() != AbstractAnimation.Type.NONE) {
            this.atlas = new TextureAtlas();
        }

        if (animation.type() == AbstractAnimation.Type.MODEL) {
            BaseMod.subscribe(this);
        }
    }

    public void playDeathAnimation() {
        if (this.corpseImg != null) {
            this.img = this.corpseImg;
            this.renderCorpse = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.corpseImg != null) {
            this.corpseImg.dispose();
            this.corpseImg = null;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.atlas == null || this.renderCorpse) {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    drawStaticImg(sb);
                }
            } else {
                switch (this.animation.type()) {
                    case NONE:
                        this.state.update(Gdx.graphics.getDeltaTime());
                        this.state.apply(this.skeleton);
                        this.skeleton.updateWorldTransform();
                        this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
                        this.skeleton.setColor(this.tint.color);
                        this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                        sb.end();
                        CardCrawlGame.psb.begin();
                        sr.draw(CardCrawlGame.psb, this.skeleton);
                        CardCrawlGame.psb.end();
                        sb.begin();
                        sb.setBlendFunction(770, 771);
                        break;
                    case MODEL:
                        BaseMod.publishAnimationRender(sb);
                        break;
                    case SPRITE:
                        this.animation.setFlip(this.flipHorizontal, this.flipVertical);
                        this.animation.renderSprite(sb, this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
                }
            }

            if (this == (AbstractDungeon.getCurrRoom()).monsters.hoveredMonster &&
                    this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {
                    drawStaticImg(sb);
                    sb.setBlendFunction(770, 771);
                }
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            renderHealth(sb);
            try {
                renderNameHandle.invoke(this, sb);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (!this.hasTakenTurn()) {
            moves.render(sb);
        }
    }

    private void drawStaticImg(SpriteBatch sb) {
        sb.draw(this.img,
                this.drawX - this.img.getWidth() * scale * Settings.scale / 2.0F + this.animX,
                this.drawY + this.animY,
                this.img.getWidth() * scale * Settings.scale,
                this.img.getHeight() * scale * Settings.scale,
                0,
                0,
                this.img.getWidth(),
                this.img.getHeight(),
                this.flipHorizontal,
                this.flipVertical);
    }

    @Override
    public void receiveModelRender(ModelBatch batch, Environment env) {
        this.animation.renderModel(batch, env);
    }
}
