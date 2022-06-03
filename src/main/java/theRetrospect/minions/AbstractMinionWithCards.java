package theRetrospect.minions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import theRetrospect.subscribers.EndOfTurnCardSubscriber;
import theRetrospect.util.CallbackUtils;
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
    public final List<AbstractCard> cardIntents = new ArrayList<>();

    /**
     * All the cards that this minion has.
     */
    public final List<AbstractCard> cards = new ArrayList<>();

    public AbstractMinionWithCards(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        cardStack = new HoverableCardStack(cardIntents, this.intentHb.cX, this.intentHb.cY);
    }

    public void addCard(AbstractCard card) {
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
        this.cards.addAll(cards);
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
        cardStack.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (!this.renderCorpse)
            cardStack.render(sb);
    }
}
