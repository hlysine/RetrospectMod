package theRetrospect.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.RetrospectMod;
import theRetrospect.minions.AbstractMinionWithCards;
import theRetrospect.patches.CardAddFieldsPatch;
import theRetrospect.patches.CardReturnToMinionPatch;
import theRetrospect.powers.TimerPower;

import java.util.List;
import java.util.Optional;

public class CardUtils {

    public static boolean getHovered(AbstractCard card) {
        return ReflectionHacks.getPrivate(card, AbstractCard.class, "hovered");
    }

    public static CardPlaySource getPlaySource(AbstractCard card) {
        CardPlaySource source = CardAddFieldsPatch.playSource.get(card);

        // Return the specified play source, if any
        if (source != null) return source;

        // Do a best-effort guess if none is available
        if (card.isInAutoplay) return CardPlaySource.CARD;
        else return CardPlaySource.PLAYER;
    }

    public static void setPlaySource(AbstractCard card, CardPlaySource source) {
        CardAddFieldsPatch.playSource.set(card, source);
    }

    public static List<AbstractGameAction> getFollowUpActions(AbstractCard card) {
        return CardAddFieldsPatch.followUpActions.get(card);
    }

    /**
     * Remove all actions from the card.
     *
     * @param card The card to remove actions from.
     */
    public static void clearFollowUpActions(AbstractCard card) {
        CardAddFieldsPatch.followUpActions.get(card).clear();
    }

    /**
     * Set an action to be executed after the card is played and all the onAfterCardUse triggers are complete.
     * The action is only executed once and is cleared afterwards.
     * Note that if dontTriggerAfterUse is true, the action is cleared without being executed.
     *
     * @param card           The card to store the action in.
     * @param actionAfterUse The action to be executed.
     */
    public static void addFollowUpAction(AbstractCard card, AbstractGameAction actionAfterUse) {
        CardAddFieldsPatch.followUpActions.get(card).add(actionAfterUse);
    }

    /**
     * Remove an existing action. Does nothing if the action does not exist on the card.
     *
     * @param card           The card to remove the action from.
     * @param actionAfterUse The action to be removed.
     * @return True if the action was in the card.
     */
    public static boolean removeFollowUpAction(AbstractCard card, AbstractGameAction actionAfterUse) {
        return CardAddFieldsPatch.followUpActions.get(card).remove(actionAfterUse);
    }

    public static AbstractMinionWithCards getReturnToMinion(AbstractCard card) {
        return CardAddFieldsPatch.returnToMinion.get(card);
    }

    public static void setReturnToMinion(AbstractCard card, AbstractMinionWithCards timeline) {
        CardAddFieldsPatch.returnToMinion.set(card, timeline);
    }

    /**
     * Make a copy of the card with the same stats, position and powers applied.
     *
     * @param card The card to be copied.
     * @return A new copy of the card.
     */
    public static AbstractCard makeAdvancedCopy(AbstractCard card) {
        AbstractCard newCard = card.makeStatEquivalentCopy();
        newCard.current_x = card.current_x;
        newCard.target_x = newCard.current_x;
        newCard.current_y = card.current_y;
        newCard.target_y = newCard.current_y;
        newCard.applyPowers();
        return newCard;
    }

    public static int calculateXCostEffect(AbstractCard card) {
        int effect = EnergyPanel.totalCount;
        if (card.energyOnUse != -1) {
            effect = card.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }

        return effect;
    }

    public static void soulReturnToMinion(SoulGroup souls, AbstractCard card, AbstractMinionWithCards timeline) {
        boolean needMoreSouls = true;
        List<Soul> soulList = ReflectionHacks.getPrivate(souls, SoulGroup.class, "souls");
        for (Soul s : soulList) {
            if (s.isReadyForReuse) {
                card.untip();
                card.unhover();
                setUpSoulForMinion(s, card, timeline);
                needMoreSouls = false;
                break;
            }
        }
        if (needMoreSouls) {
            RetrospectMod.logger.info("Not enough souls, creating...");
            Soul s = new Soul();
            setUpSoulForMinion(s, card, timeline);
            soulList.add(s);
        }
    }

    private static void setUpSoulForMinion(Soul soul, AbstractCard card, AbstractMinionWithCards timeline) {
        soul.card = card;
        soul.group = null;
        ReflectionHacks.setPrivate(soul, Soul.class, "pos", new Vector2(card.current_x, card.current_y));
        ReflectionHacks.setPrivate(soul, Soul.class, "target", new Vector2(timeline.drawX, timeline.drawY));
        ReflectionHacks.privateMethod(Soul.class, "setSharedVariables").invoke(soul);
        timeline.addCard(card);
        Optional<TimerPower> power = timeline.powers.stream().filter(p -> p instanceof TimerPower).findFirst().map(p -> (TimerPower) p);
        power.ifPresent(TimerPower::refresh);
        CardReturnToMinionPatch.SoulAddFieldPatch.returnToMinion.set(soul, timeline);
        ReflectionHacks.setPrivate(soul, Soul.class, "rotation", card.angle + 270.0F);
        ReflectionHacks.setPrivate(soul, Soul.class, "rotateClockwise", true);
        final float START_VELOCITY = ReflectionHacks.getPrivateStatic(Soul.class, "START_VELOCITY");
        if (Settings.FAST_MODE) {
            ReflectionHacks.setPrivate(soul, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(4.0F, 6.0F));
        } else {
            ReflectionHacks.setPrivate(soul, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(1.0F, 4.0F));
        }
    }
}
