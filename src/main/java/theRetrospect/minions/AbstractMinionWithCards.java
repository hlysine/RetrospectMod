package theRetrospect.minions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import theRetrospect.powers.EndOfTurnCardPlayingPower;
import theRetrospect.util.HoverableCardStack;

import java.util.ArrayList;
import java.util.List;

public class AbstractMinionWithCards extends AbstractFriendlyMonster {

    /**
     * A stack of cards rendered on top of this minion. Used to visualize {@link AbstractMinionWithCards#cardIntents}
     */
    public final HoverableCardStack cardStack;

    /**
     * A list of cards that this minion intends to play.
     */
    public final List<AbstractCard> cardIntents;

    /**
     * All the cards that this minion has.
     */
    public final List<AbstractCard> cards;

    public float target_x;
    public float target_y;

    public AbstractMinionWithCards(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        target_x = drawX;
        target_y = drawY;
        cards = new ArrayList<>();
        cardIntents = new ArrayList<>();
        cardStack = new HoverableCardStack(cardIntents, this.intentHb.cX, this.intentHb.cY);
    }

    public void setCards(List<AbstractCard> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }

    public void setCardIntents(List<AbstractCard> cardIntents) {
        this.cardIntents.clear();
        this.cardIntents.addAll(cardIntents);
    }

    public void endOfTurnPlayCards() {
        for (AbstractPower power : powers) {
            if (power instanceof EndOfTurnCardPlayingPower) {
                EndOfTurnCardPlayingPower cardPlayingPower = (EndOfTurnCardPlayingPower) power;
                cardPlayingPower.endOfTurnPlayCards();
            }
        }
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
        this.drawX = MathHelper.orbLerpSnap(this.drawX, this.target_x);
        this.drawY = MathHelper.orbLerpSnap(this.drawY, this.target_y);
        refreshHitboxLocation();
        refreshIntentHbLocation();
        this.cardStack.x = this.intentHb.cX;
        this.cardStack.y = this.intentHb.cY;
    }

    @Override
    public void update() {
        super.update();
        cardStack.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        cardStack.render(sb);
    }
}
