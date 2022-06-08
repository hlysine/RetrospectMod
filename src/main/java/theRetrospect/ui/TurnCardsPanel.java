package theRetrospect.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.effects.TurnCardsParticleEffect;

public class TurnCardsPanel extends AbstractPanel {
    public static final String ID = RetrospectMod.makeID(TurnCardsPanel.class.getSimpleName());
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(ID);
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    private static final float COUNT_CIRCLE_W = 128.0F * Settings.scale;
    private final Hitbox hb = new Hitbox(0.0F, 0.0F, 100.0F * Settings.scale, 100.0F * Settings.scale);
    public static float energyVfxTimer = 0.0F;
    public static final Color textColor = CardHelper.getColor(170, 40, 200);

    public TurnCardsPanel() {
        super(
                Settings.WIDTH - 70.0F * Settings.scale, 284.0F * Settings.scale,
                Settings.WIDTH + 100.0F * Settings.scale, 284.0F * Settings.scale,
                0.0F, 0.0F,
                null,
                false
        );
    }

    public void updatePositions() {
        super.updatePositions();

        if (!this.isHidden &&
                AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0) {
            this.hb.update();
            updateVfx();
        }

        if (this.hb.hovered &&
                (!AbstractDungeon.isScreenUp ||
                        AbstractDungeon.screen == UIManager.TURN_CARDS_VIEW ||
                        AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT ||
                        (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD && AbstractDungeon.overlayMenu.combatPanelsShown))) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }


        if (this.hb.clicked && AbstractDungeon.screen == UIManager.TURN_CARDS_VIEW) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            CardCrawlGame.sound.play("DECK_CLOSE");
            AbstractDungeon.closeCurrentScreen();

            return;
        }

        if (this.hb.clicked && AbstractDungeon.overlayMenu.combatPanelsShown && AbstractDungeon.getMonsters() != null &&
                !AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.player.isDead &&
                !AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {

            this.hb.clicked = false;
            this.hb.hovered = false;

            if (AbstractDungeon.isScreenUp) {
                if (AbstractDungeon.previousScreen == null) {
                    AbstractDungeon.previousScreen = AbstractDungeon.screen;
                }
            } else {
                AbstractDungeon.previousScreen = null;
            }

            openTurnCardsPile();
        }
    }

    private void openTurnCardsPile() {
        if (AbstractDungeon.player.hoveredCard != null) {
            AbstractDungeon.player.releaseCard();
        }

        AbstractDungeon.dynamicBanner.hide();
        UIManager.turnCardsViewScreen.open();
        this.hb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    private void updateVfx() {
        energyVfxTimer -= Gdx.graphics.getDeltaTime();
        if (energyVfxTimer < 0.0F && !Settings.hideLowerElements) {
            AbstractDungeon.effectList.add(new TurnCardsParticleEffect(this.current_x, this.current_y));
            energyVfxTimer = 0.05F;
        }
    }


    public void render(SpriteBatch sb) {
        if (!AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
            this.hb.move(this.current_x, this.current_y);

            String msg = Integer.toString(AbstractDungeon.actionManager.cardsPlayedThisTurn.size());
            sb.setColor(Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x - COUNT_CIRCLE_W / 2.0F, this.current_y - COUNT_CIRCLE_W / 2.0F, COUNT_CIRCLE_W, COUNT_CIRCLE_W);


            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, this.current_x, this.current_y + 2.0F * Settings.scale, textColor);

            this.hb.render(sb);
            if (this.hb.hovered && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp)
                TipHelper.renderGenericTip(
                        1550.0F * Settings.scale, 450.0F * Settings.scale,
                        LABEL[0],
                        MSG[0]
                );
        }
    }
}

