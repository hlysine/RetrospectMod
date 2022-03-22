package theRetrospect.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class IgnoreCapacityChannelAction
        extends AbstractGameAction {
    private final AbstractOrb orbType;
    /**
     * Whether an old orb is allowed to be evoked to make room for a new orb.
     */
    private boolean autoEvoke = false;

    public IgnoreCapacityChannelAction(AbstractOrb newOrbType) {
        this(newOrbType, true);
    }

    /**
     * Construct this action
     * @param newOrbType The orb to channel
     * @param autoEvoke Whether an old orb is allowed to be evoked to make room for a new orb
     */
    public IgnoreCapacityChannelAction(AbstractOrb newOrbType, boolean autoEvoke) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.orbType = newOrbType;
        this.autoEvoke = autoEvoke;
    }

    public void update() {
        // increase capacity if possible
        if (AbstractDungeon.player.maxOrbs == 10) {
            if (!AbstractDungeon.player.hasEmptyOrb()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "There are too many #rtimelines.", true)); // todo: extract string

                this.isDone = true;
                return;
            }
        } else {
            AbstractDungeon.player.increaseMaxOrbSlots(1, false);
            CardCrawlGame.sound.playA("GUARDIAN_ROLL_UP", 1.0F);
        }

        if (this.autoEvoke) {
            AbstractDungeon.player.channelOrb(this.orbType);
        } else {
            // Evoking is not allowed
            // Only channel if there is an empty slot
            for (AbstractOrb o : AbstractDungeon.player.orbs) {
                if ((o instanceof EmptyOrbSlot)) {
                    AbstractDungeon.player.channelOrb(this.orbType);
                    break;
                }
            }
        }
        tickDuration();
        this.isDone = true;
    }
}
