package theRetrospect.minions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import theRetrospect.RetrospectMod;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.subscribers.MinionCardsChangedSubscriber;
import theRetrospect.ui.UIManager;
import theRetrospect.util.CallbackUtils;
import theRetrospect.util.HoverableCardStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbstractMinionWithCards extends AbstractFriendlyMonster {

    public static final int MAX_CARDS = 999;

    /**
     * A stack of cards rendered on top of this minion. Used to visualize {@link AbstractMinionWithCards#cardIntents}
     */
    public final HoverableCardStack cardStack;

    /**
     * A list of cards that this minion intends to play.
     */
    public final List<AbstractCard> cardIntents = new ArrayList<>();

    /**
     * All the cards that this minion has.
     */
    public final List<AbstractCard> cards = new ArrayList<>();

    private static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);

    public AbstractMinionWithCards(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        cardStack = new HoverableCardStack(cardIntents, this.intentHb.cX, this.intentHb.cY);
    }

    public void addCard(AbstractCard card) {
        if (this.cards.size() >= MAX_CARDS) return;
        card.current_x = card.target_x = this.drawX;
        card.current_y = card.target_y = this.drawY;
        this.cards.add(card);
    }

    public void setCards(List<AbstractCard> cards) {
        this.cards.clear();
        cards.forEach(card -> {
            card.current_x = card.target_x = this.drawX;
            card.current_y = card.target_y = this.drawY;
        });
        for (AbstractCard card : cards) {
            if (this.cards.size() >= MAX_CARDS) break;
            this.cards.add(card);
        }
    }

    public void triggerCardsChange() {
        Optional<MinionCardsChangedSubscriber> power = this.powers.stream()
                .filter(p -> p instanceof MinionCardsChangedSubscriber)
                .findFirst()
                .map(p -> (MinionCardsChangedSubscriber) p);
        power.ifPresent(MinionCardsChangedSubscriber::onCardsChanged);
    }

    public void setCardIntents(List<AbstractCard> cardIntents) {
        this.cardIntents.clear();
        this.cardIntents.addAll(cardIntents);
    }

    public void triggerOnEndOfTurnForPlayingCard(Runnable next) {
        CallbackUtils.ForEachLoop(powers, (power, nxt) -> {
            if (power instanceof EndOfTurnCardSubscriber) {
                EndOfTurnCardSubscriber cardPlayingPower = (EndOfTurnCardSubscriber) power;
                cardPlayingPower.triggerOnEndOfTurnForPlayingCard(nxt);
            } else {
                nxt.run();
            }
        }, next);
    }

    @Override
    public void applyPowers() {
        for (AbstractCard card : cards) {
            card.applyPowers();
        }
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();
        this.cardStack.x = this.intentHb.cX;
        this.cardStack.y = this.intentHb.cY;
    }

    @Override
    public void update() {
        super.update();
        this.hb.update();
        if (this.hb.hovered && !AbstractDungeon.isScreenUp && !PeekButton.isPeeking) {
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }
        if (this.hb.clicked &&
                AbstractDungeon.overlayMenu.combatPanelsShown &&
                !AbstractDungeon.isScreenUp &&
                !PeekButton.isPeeking &&
                AbstractDungeon.getMonsters() != null &&
                !AbstractDungeon.getMonsters().areMonstersDead() &&
                !AbstractDungeon.player.isDead &&
                !this.isDeadOrEscaped() && !this.isDead &&
                !cards.isEmpty()) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            RetrospectMod.logger.info("Open card view screen for timeline");

            if (AbstractDungeon.isScreenUp) {
                if (AbstractDungeon.previousScreen == null) {
                    AbstractDungeon.previousScreen = AbstractDungeon.screen;
                }
            } else {
                AbstractDungeon.previousScreen = null;
            }

            openMinionCardsViewScreen();
        }
        cardStack.update();
    }

    private void openMinionCardsViewScreen() {
        if (AbstractDungeon.player.hoveredCard != null) {
            AbstractDungeon.player.releaseCard();
        }

        AbstractDungeon.dynamicBanner.hide();
        UIManager.minionCardsViewScreen.open(cards);
        this.hb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hb.hovered &&
                AbstractDungeon.overlayMenu.combatPanelsShown &&
                !AbstractDungeon.isScreenUp &&
                !PeekButton.isPeeking &&
                AbstractDungeon.getMonsters() != null &&
                !AbstractDungeon.getMonsters().areMonstersDead() &&
                !AbstractDungeon.player.isDead &&
                !this.isDeadOrEscaped() && !this.isDead &&
                !cards.isEmpty()) {
            renderPeekButton(sb);
        }
        if (!this.renderCorpse)
            cardStack.render(sb);
    }

    private void renderPeekButton(SpriteBatch sb) {
        sb.setColor(HOVER_BLEND_COLOR);
        sb.draw(ImageMaster.PEEK_BUTTON, this.hb.cX - 48, this.hb.cY - 48, 48, 48, 96, 96, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
    }
}
