package theRetrospect.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MetricsPatch {
    public static final String UPLOAD_URL = "http://127.0.0.1:8887";

    private static final Logger logger = LogManager.getLogger(MetricsPatch.class.getName());

    @SpirePatch(
            clz = GameOverScreen.class,
            method = "shouldUploadMetricData"
    )
    public static class ShouldUploadMetricDataPatch {
        public static boolean Postfix(boolean __result) { // it is important to define this parameter so that the return value is replaced
            if (Settings.UPLOAD_DATA)
                logger.info("Allowing metrics upload for modded content");
            return Settings.UPLOAD_DATA;
        }
    }

    @SpirePatch(
            clz = Metrics.class,
            method = "gatherAllData"
    )
    public static class MetricsGatherAllDataPatch {
        public static void Prefix() {
            logger.info("Gathering metrics data");
        }
    }

    @SpirePatch(
            clz = Metrics.class,
            method = "run"
    )
    public static class MetricsRunPatch {
        public static void Prefix(Metrics __instance) {
            logger.info("Patched Metrics.run");
            if (Settings.isModded) {
                logger.info("Uploading modded metrics");
                switch (__instance.type) {
                    case UPLOAD_CRASH:
                        gatherAllDataAndSend(__instance, __instance.death, false, __instance.monsters);
                        break;
                    case UPLOAD_METRICS:
                        gatherAllDataAndSend(__instance, __instance.death, __instance.trueVictory, __instance.monsters);
                        break;
                }
            }
        }

        private static void gatherAllDataAndSend(Metrics instance, boolean death, boolean trueVictor, MonsterGroup monsters) {
            if (Settings.UPLOAD_DATA) {
                ReflectionHacks.privateMethod(Metrics.class, "gatherAllData", boolean.class, boolean.class, MonsterGroup.class).invoke(instance, death, trueVictor, monsters);
                ReflectionHacks.privateMethod(Metrics.class, "sendPost", String.class, String.class).invoke(instance, UPLOAD_URL, null);
                logger.info("Modded metrics sent to " + UPLOAD_URL);
            }
        }
    }
}
