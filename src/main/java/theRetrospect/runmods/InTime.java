package theRetrospect.runmods;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.RunModStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.util.DamageInfoUtils;

public class InTime extends CustomDailyMod {
    public static final String ID = RetrospectMod.makeID(InTime.class.getSimpleName());
    public static final String IMG = RetrospectMod.makeRunModPath("in_time.png");
    private static final RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString(ID);
    public static final String NAME = modStrings.NAME, DESC = modStrings.DESCRIPTION;

    public static final float HP_LOSS_INTERVAL = 0.5f;

    private static final Logger logger = LogManager.getLogger(InTime.class);

    public InTime() {
        super(ID, NAME, DESC, IMG, false);
    }

    private static long lastTime = 0;
    private static float cumulativeTime = 0;

    private static boolean isRunning = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        lastTime = System.nanoTime();
        cumulativeTime = 0;
    }

    public static void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    public static void update() {
        if (!isRunning) {
            return;
        }

        // Time since last update in seconds.
        float deltaTime;

        long time = System.nanoTime();
        if (lastTime == 0) {
            lastTime = time;
            deltaTime = 0;
        } else {
            deltaTime = (time - lastTime) / 1.0E9F;
            lastTime = time;
        }

        cumulativeTime += deltaTime;
        if (cumulativeTime > HP_LOSS_INTERVAL) {
            cumulativeTime -= HP_LOSS_INTERVAL;
            tick();
        }
    }

    public static void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RunnableAction(InTime::start));
    }

    public static void atEndOfTurn() {
        stop();
    }

    public static void onAttack(int damage) {
        AbstractDungeon.player.heal(damage, true);
    }

    private static void tick() {
        logger.info("InTime tick");
        if (ModHelper.isModEnabled(ID)
                && AbstractDungeon.player != null
                && !AbstractDungeon.player.isDeadOrEscaped()
                && AbstractDungeon.player.currentHealth > 0)
            AbstractDungeon.player.damage(
                    DamageInfoUtils.withNoVisualEffect(new DamageInfo(null, 1, DamageInfo.DamageType.HP_LOSS))
            );
        else
            stop();
    }
}
