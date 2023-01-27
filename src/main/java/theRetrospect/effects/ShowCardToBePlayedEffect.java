package theRetrospect.effects;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theRetrospect.util.CardUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowCardToBePlayedEffect extends AbstractGameEffect {
    private final List<AbstractCard> cardsToPlay;
    private static final float PADDING = 22.5F * Settings.scale;
    private final float origin_x;
    private final float origin_y;
    private final boolean showOnRight;

    public ShowCardToBePlayedEffect(List<AbstractCard> cardsToPlay, float origin_x, float origin_y) {
        showOnRight = origin_x < Settings.WIDTH / 2f;
        this.origin_x = origin_x;
        this.origin_y = origin_y;

        for (AbstractCard card : cardsToPlay) {
            card.current_x = origin_x;
            card.current_y = origin_y;
        }

        this.cardsToPlay = new ArrayList<>(cardsToPlay);
        for (AbstractCard card : this.cardsToPlay) {
            CardUtils.addFollowUpActionToTop(card, new AbstractGameAction() {
                @Override
                public void update() {
                    ShowCardToBePlayedEffect.this.cardsToPlay.remove(card);
                    this.isDone = true;
                }
            }, false, 1000);
        }
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        }
        if (cardsToPlay.size() == 0) {
            this.isDone = true;
            return;
        }
        for (int i = 0; i < cardsToPlay.size(); i++) {
            AbstractCard card = cardsToPlay.get(i);

            card.update();
            if (!(boolean) ReflectionHacks.getPrivate(card, AbstractCard.class, "darken")) {
                if (showOnRight)
                    card.target_x = origin_x + (PADDING + AbstractCard.IMG_WIDTH) * (i + 1);
                else
                    card.target_x = origin_x - (PADDING + AbstractCard.IMG_WIDTH) * (i + 1);
                card.target_y = origin_y + (Settings.HEIGHT / 2f - origin_y) / 5f;
                card.targetDrawScale = 0.75f;
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone && AbstractDungeon.ftue == null)
            for (AbstractCard card : this.cardsToPlay) {
                card.render(sb);
            }
    }

    public void dispose() {
    }
}