package theRetrospect.minions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import theRetrospect.RetrospectMod;

import java.util.List;

public class TimelineMinion extends AbstractFriendlyMonster {

    // todo: clean up card field
    // todo: better card hover
    // todo: render next played card on top

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String IMG = "theRetrospectResources/images/char/retrospectCharacter/Spriter/object1.png";

    public static final String NAME = minionStrings.NAMES[0];

    private final List<AbstractCard> cards;

    public TimelineMinion(List<AbstractCard> cards, int offsetX, int offsetY) {
        super(NAME, ID, 20, 0, 0, 240, 240, IMG, offsetX, offsetY);
        this.cards = cards;
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        for (MinionMove m : this.moves.getMoves()) {
            if (m instanceof AutoMinionMove) {
                AutoMinionMove move = (AutoMinionMove) m;
                move.moveActions.run();
                AbstractDungeon.effectList.add(new CardFlashVfx(cards.get(0)));
            }
        }
        this.clearMoves();
    }

    @Override
    public void update() {
        super.update();
        boolean hasHover = false;
        float offsetX = (cards.size() - 1) * 20f / 2;
        for (int i = 0; i < cards.size(); i++) {
            AbstractCard card = cards.get(i);
            card.update();
            card.current_x = this.intentHb.cX - offsetX + 20f * i;
            card.current_y = this.intentHb.cY;
            if (!hasHover && card.isHoveredInHand(card.drawScale)) {
                card.hover();
                hasHover = true;
            } else {
                card.unhover();
                card.targetDrawScale = 0.33f;
            }
        }
        if (this.hb.hovered) {
            TipHelper.renderGenericTip(this.hb.x + this.hb.width + 15.0F * Settings.scale, this.hb.y, NAME, "This #ytimeline contains #b3 cards. NL #yReplay #b1 card at the end of your turn. NL #yCollapse this #ytimeline when all cards are replayed.");
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float offsetX = (cards.size() - 1) * 20f / 2;
        for (int i = 0; i < cards.size(); i++) {
            AbstractCard card = cards.get(i);
            card.current_x = this.intentHb.cX - offsetX + 20f * i;
            card.current_y = this.intentHb.cY;
            card.render(sb);
        }
    }
}
