package theRetrospect.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import theRetrospect.minions.TimelineMinion;

import java.util.ArrayList;
import java.util.Optional;

public class TimelineTargeting extends TargetingHandler<AbstractCreature> {
    @SpireEnum
    public static AbstractCard.CardTarget TIMELINE;

    public static AbstractCreature getTarget(AbstractCard card) {
        return CustomTargeting.getCardTarget(card);
    }

    private AbstractCreature hovered = null;

    @Override
    public boolean hasTarget() {
        return hovered != null;
    }

    @Override
    public void updateHovered() {
        hovered = null;

        MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
        for (AbstractMonster minion : minions.monsters) {
            if (!minion.isDeadOrEscaped() && minion instanceof TimelineMinion) {
                minion.hb.update();
                if (minion.hb.hovered) {
                    hovered = minion;
                    break;
                }
            }
        }
    }

    @Override
    public AbstractCreature getHovered() {
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

    //Keyboard support is entirely optional, but this is an example based on how the basegame implements it
    @Override
    public void setDefaultTarget() {
        Optional<AbstractMonster> minion = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream().filter(m -> m instanceof TimelineMinion).findFirst();
        minion.ifPresent(abstractMonster -> hovered = abstractMonster);
    }

    @Override
    public int getDefaultTargetX() {
        Optional<AbstractMonster> minion = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream().filter(m -> m instanceof TimelineMinion).findFirst();
        return minion.map(abstractMonster -> (int) abstractMonster.hb.cX).orElse(Settings.WIDTH / 2);
    }

    @Override
    public int getDefaultTargetY() {
        Optional<AbstractMonster> minion = MinionUtils.getMinions(AbstractDungeon.player).monsters.stream().filter(m -> m instanceof TimelineMinion).findFirst();
        return minion.map(abstractMonster -> (int) abstractMonster.hb.cY).orElse(Settings.HEIGHT / 2);
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
            MonsterGroup minions = MinionUtils.getMinions(AbstractDungeon.player);
            ArrayList<AbstractMonster> sortedMonsters = new ArrayList<>(minions.monsters);

            sortedMonsters.removeIf(AbstractCreature::isDeadOrEscaped);
            sortedMonsters.removeIf(m -> !(m instanceof TimelineMinion));

            AbstractCreature newTarget;

            if (sortedMonsters.isEmpty()) return;

            sortedMonsters.sort(AbstractMonster.sortByHitbox);

            if (this.hovered == null) {
                newTarget = sortedMonsters.get(0);
            } else {
                @SuppressWarnings("SuspiciousMethodCalls") int currentTargetIndex = sortedMonsters.indexOf(hovered);

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
