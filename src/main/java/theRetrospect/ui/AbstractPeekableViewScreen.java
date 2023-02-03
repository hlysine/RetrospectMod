package theRetrospect.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

import java.util.List;

public abstract class AbstractPeekableViewScreen extends AbstractViewScreen {
    public PeekButton peekButton = new PeekButton();

    @Override
    public void update() {
        this.peekButton.update();
        if (!PeekButton.isPeeking)
            super.update();
    }

    @Override
    protected void updateScrolling() {
        if (!PeekButton.isPeeking)
            super.updateScrolling();
    }

    @Override
    public void open(List<AbstractCard> cards) {
        super.open(cards);

        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            this.peekButton.hideInstantly();
            this.peekButton.show();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!PeekButton.isPeeking)
            super.render(sb);

        this.peekButton.render(sb);
    }

    @Override
    protected boolean shouldShowScrollBar() {
        return super.shouldShowScrollBar() && !PeekButton.isPeeking;
    }
}
