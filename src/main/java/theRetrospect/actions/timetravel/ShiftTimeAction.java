package theRetrospect.actions.timetravel;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.FtueTip;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.AbstractBaseCard;
import theRetrospect.effects.TimeShiftEffect;
import theRetrospect.effects.TimeTravelEffect;
import theRetrospect.effects.TimelineCircleEffect;
import theRetrospect.mechanics.timetravel.TimeManager;
import theRetrospect.minions.TimelineMinion;
import theRetrospect.util.TimelineUtils;

public class ShiftTimeAction extends AbstractGameAction {

    public static final String ID = RetrospectMod.makeID(AbstractBaseCard.class.getSimpleName());

    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(RetrospectMod.makeID(ShiftTimeAction.class.getSimpleName()));

    private final AbstractCard shiftCard;
    private final TimelineMinion target;
    private final AbstractMonster persistingMonster;
    private boolean shiftDone = false;
    private boolean endingDone = false;

    public ShiftTimeAction(AbstractCard shiftCard, TimelineMinion target, AbstractMonster persistingMonster) {
        this.shiftCard = shiftCard;
        this.target = target;
        this.persistingMonster = persistingMonster;
        this.duration = this.startDuration = Settings.FAST_MODE ? TimeTravelEffect.SHORT_DURATION : TimeTravelEffect.LONG_DURATION;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (this.duration == this.startDuration) {
            if (persistingMonster != null) {
                AbstractDungeon.topLevelEffects.add(new TimeShiftEffect(
                        new Vector2(persistingMonster.hb.cX, persistingMonster.hb.cY),
                        Math.max(persistingMonster.hb.height, persistingMonster.hb.width) / 2));
            } else {
                AbstractDungeon.topLevelEffects.add(new TimeShiftEffect());
            }
        }
        if (this.duration < this.startDuration / 2 && !shiftDone) {
            shiftDone = true;

            TimeManager.shiftTime(target, shiftCard, persistingMonster);

            TimelineUtils.repositionTimelines(player);

            CardCrawlGame.sound.play("CARD_POWER_IMPACT", 0.1f);
        }
        if (this.duration < this.startDuration / 5 && !endingDone) {
            endingDone = true;

            AbstractDungeon.effectList.add(new TimelineCircleEffect(target));

            if (!TipTracker.tips.get(TimelineUtils.SHIFT_TIP)) {
                AbstractDungeon.ftue = new FtueTip(
                        tutorialStrings.LABEL[0],
                        tutorialStrings.TEXT[0],
                        target.target_x + target.hb.width / 2 + 300,
                        target.target_y,
                        FtueTip.TipType.CREATURE
                );
                ReflectionHacks.setPrivate(AbstractDungeon.ftue, AbstractDungeon.ftue.getClass(), "m", target);

                TipTracker.neverShowAgain(TimelineUtils.SHIFT_TIP);
            }
        }
        tickDuration();
    }
}
