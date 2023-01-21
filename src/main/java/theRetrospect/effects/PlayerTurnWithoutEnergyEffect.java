package theRetrospect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

public class PlayerTurnWithoutEnergyEffect extends AbstractGameEffect {
    private static final float DUR = 2.0F;
    private static final float HEIGHT_DIV_2 = (float) Settings.HEIGHT / 2.0F;
    private static final float WIDTH_DIV_2 = (float) Settings.WIDTH / 2.0F;
    private final Color bgColor;
    private static final float TARGET_HEIGHT = 150.0F * Settings.scale;
    private float currentHeight = 0.0F;
    private final String turnMessage;
    private static final float MAIN_MSG_OFFSET_Y = 20.0F * Settings.scale;
    private static final float TURN_MSG_OFFSET_Y = -30.0F * Settings.scale;
    private final Color turnMessageColor = new Color(0.7F, 0.7F, 0.7F, 0.0F);

    public PlayerTurnWithoutEnergyEffect() {
        this.duration = DUR;
        this.startingDuration = DUR;
        this.bgColor = new Color(AbstractDungeon.fadeColor.r / 2.0F, AbstractDungeon.fadeColor.g / 2.0F, AbstractDungeon.fadeColor.b / 2.0F, 0.0F);
        this.color = Settings.GOLD_COLOR.cpy();
        this.color.a = 0.0F;
        if (Settings.usesOrdinal) {
            this.turnMessage = GameActionManager.turn + getOrdinalNaming(GameActionManager.turn) + BattleStartEffect.TURN_TXT;
        } else if (Settings.language == GameLanguage.VIE) {
            this.turnMessage = BattleStartEffect.TURN_TXT + " " + GameActionManager.turn;
        } else {
            this.turnMessage = GameActionManager.turn + BattleStartEffect.TURN_TXT;
        }

        CardCrawlGame.sound.play("TURN_EFFECT");
        AbstractDungeon.getMonsters().showIntent();
        this.scale = 1.0F;
    }

    public static String getOrdinalNaming(int i) {
        return i % 100 != 11 && i % 100 != 12 && i % 100 != 13 ? (new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"})[i % 10] : "th";
    }

    public void update() {
        if (this.duration > 1.5F) {
            this.currentHeight = MathUtils.lerp(this.currentHeight, TARGET_HEIGHT, Gdx.graphics.getDeltaTime() * 3.0F);
        } else if (this.duration < 0.5F) {
            this.currentHeight = MathUtils.lerp(this.currentHeight, 0.0F, Gdx.graphics.getDeltaTime() * 3.0F);
        }

        if (this.duration > 1.5F) {
            this.scale = Interpolation.exp10In.apply(1.0F, 3.0F, (this.duration - 1.5F) * 2.0F);
            this.color.a = Interpolation.exp10In.apply(1.0F, 0.0F, (this.duration - 1.5F) * 2.0F);
        } else if (this.duration < 0.5F) {
            this.scale = Interpolation.pow3In.apply(0.9F, 1.0F, this.duration * 2.0F);
            this.color.a = Interpolation.pow3In.apply(0.0F, 1.0F, this.duration * 2.0F);
        }

        this.bgColor.a = this.color.a * 0.75F;
        this.turnMessageColor.a = this.color.a;
        if (Settings.FAST_MODE) {
            this.duration -= Gdx.graphics.getDeltaTime();
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            sb.setColor(this.bgColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, HEIGHT_DIV_2 - this.currentHeight / 2.0F, (float) Settings.WIDTH, this.currentHeight);
            sb.setBlendFunction(770, 1);
            FontHelper.renderFontCentered(sb, FontHelper.bannerNameFont, BattleStartEffect.PLAYER_TURN_MSG, WIDTH_DIV_2, HEIGHT_DIV_2 + MAIN_MSG_OFFSET_Y, this.color, this.scale);
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, this.turnMessage, WIDTH_DIV_2, HEIGHT_DIV_2 + TURN_MSG_OFFSET_Y, this.turnMessageColor, this.scale);
            sb.setBlendFunction(770, 771);
        }

    }

    public void dispose() {
    }
}
