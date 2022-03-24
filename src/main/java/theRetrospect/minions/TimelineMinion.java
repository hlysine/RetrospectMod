package theRetrospect.minions;

import basemod.abstracts.cardbuilder.actionbuilder.EffectActionBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashIntentEffect;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import kobting.friendlyminions.monsters.MinionMove;
import theRetrospect.RetrospectMod;

import java.util.List;

public class TimelineMinion extends AbstractFriendlyMonster {

    // todo: remove moves
    // todo: reposition card
    // todo: clean up card field
    // todo: fix card hit box

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String IMG = "theRetrospectResources/images/char/retrospectCharacter/main2.png";

    public static final String NAME = minionStrings.NAMES[0];

    private final List<AbstractCard> cards;

    public TimelineMinion(List<AbstractCard> cards, int offsetX, int offsetY) {
        super(NAME, ID, 20, 0, 0, 240, 240, IMG, offsetX, offsetY);
        this.cards = cards;
        addMoves();
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
        addMoves();
    }

    private void addMoves() {
        this.moves.addMove(new AutoMinionMove("Attack", this, new Texture("theRetrospectResources/images/ui/missing_texture.png"), "Deal 5 damage", () -> {
            AbstractMonster target = AbstractDungeon.getRandomMonster();
            DamageInfo info = new DamageInfo(this, 5, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this, target); // <--- This lets powers effect minions attacks
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
        }));
    }

    @Override
    public void update() {
        super.update();
        for (AbstractCard card : cards) {
            card.update();
            if (card.isHoveredInHand(card.drawScale))
                card.hover();
            else {
                card.unhover();
                card.targetDrawScale = 0.33f;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        for (AbstractCard card : cards) {
            card.current_x = drawX;
            card.current_y = drawY + 100;
            card.render(sb);
        }
    }
}
