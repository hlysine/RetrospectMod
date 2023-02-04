package theRetrospect.patches.timetravel;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import theRetrospect.mechanics.timetravel.TimeManager;

public class UnstableRngPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class GenerateSeedsPatch {
        public static void Postfix() {
            TimeManager.unstableRng = new Random(Settings.seed);
        }
    }

    public static class UnstableRngSavable implements CustomSavable<Integer> {
        @Override
        public Integer onSave() {
            return TimeManager.unstableRng.counter;
        }

        @Override
        public void onLoad(Integer s) {
            if (s == null) {
                TimeManager.unstableRng = new Random(Settings.seed);
            } else {
                TimeManager.unstableRng = new Random(Settings.seed, s);
            }
        }
    }
}
