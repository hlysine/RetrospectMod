package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import theRetrospect.actions.TriggerOnEndOfTurnForPlayingCard;

@SpirePatch(
        clz= GameActionManager.class,
        method="callEndOfTurnActions"
)
public class ActionManagerEndOfTurnActionsPatch {
    public static void Prefix(GameActionManager __instance)
    {
        __instance.addToBottom(new TriggerOnEndOfTurnForPlayingCard());
    }
}
