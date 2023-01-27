package theRetrospect.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class BaseTargeting<T extends AbstractCreature> extends TargetingHandler<T> {

    private static final Comparator<AbstractCreature> sortByHitbox = ((o1, o2) -> (int) (o1.hb.cX - o2.hb.cX));

    public static <T extends AbstractCreature> T getTarget(AbstractCard card) {
        return CustomTargeting.getCardTarget(card);
    }

    private T hovered = null;

    /**
     * This method should return a list of all possible targets for the targeting mode.
     * Note that the returned list may be mutated by the targeting mode, so it should be a copy of the original list.
     *
     * @return A list of all possible targets for the targeting mode.
     */
    protected abstract List<T> getTargets();

    @Override
    public boolean hasTarget() {
        return hovered != null;
    }

    @Override
    public void updateHovered() {
        hovered = null;

        for (T target : getTargets()) {
            target.hb.update();
            if (target.hb.hovered) {
                hovered = target;
                break;
            }
        }
    }

    @Override
    public T getHovered() {
        return hovered;
    }

    @Override
    public void clearHovered() {
        hovered = null;
    }

    @Override
    public void renderReticle(SpriteBatch sb) {
        if (hovered != null) {
            hovered.renderReticle(sb);
        }
    }

    //Keyboard support is entirely optional, but this is an example based on how the base game implements it
    @Override
    public void setDefaultTarget() {
        Optional<T> target = getTargets().stream().findFirst();
        target.ifPresent(creature -> hovered = creature);
    }

    @Override
    public int getDefaultTargetX() {
        Optional<T> target = getTargets().stream().findFirst();
        return target.map(creature -> (int) creature.hb.cX).orElse(Settings.WIDTH / 2);
    }

    @Override
    public int getDefaultTargetY() {
        Optional<T> target = getTargets().stream().findFirst();
        return target.map(creature -> (int) creature.hb.cY).orElse(Settings.HEIGHT / 2);
    }

    @Override
    public void updateKeyboardTarget() {
        int directionIndex = 0;

        if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
            --directionIndex;
        }

        if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
            ++directionIndex;
        }

        if (directionIndex != 0) {
            List<T> sortedMonsters = getTargets();
            if (sortedMonsters.isEmpty()) return;
            sortedMonsters.sort(sortByHitbox);

            T newTarget;
            if (this.hovered == null) {
                newTarget = sortedMonsters.get(0);
            } else {
                int currentTargetIndex = sortedMonsters.indexOf(hovered);

                int newTargetIndex = currentTargetIndex + directionIndex;
                newTargetIndex = (newTargetIndex + sortedMonsters.size()) % sortedMonsters.size();
                newTarget = sortedMonsters.get(newTargetIndex);
            }


            if (newTarget != null) {
                Hitbox target = newTarget.hb;
                Gdx.input.setCursorPosition((int) target.cX, Settings.HEIGHT - (int) target.cY); //cursor y position is inverted for some reason :)
                hovered = newTarget;
                ReflectionHacks.setPrivate(AbstractDungeon.player, AbstractPlayer.class, "isUsingClickDragControl", true);
                AbstractDungeon.player.isDraggingCard = true;
            }

            if (hovered instanceof AbstractMonster && hovered.halfDead) {
                hovered = null;
            }
        }
    }
}
