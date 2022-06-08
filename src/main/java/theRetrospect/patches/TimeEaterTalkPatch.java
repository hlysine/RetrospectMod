package theRetrospect.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import theRetrospect.RetrospectMod;
import theRetrospect.characters.TheRetrospect;

@SpirePatch(
        clz = TimeEater.class,
        method = "takeTurn"
)
public class TimeEaterTalkPatch {
    public static void Prefix(TimeEater __instance, @ByRef boolean[] ___firstTurn) {
        if (___firstTurn[0]) {
            if (AbstractDungeon.player.chosenClass == TheRetrospect.Enums.THE_RETROSPECT) {
                MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(RetrospectMod.makeID(TimeEater.class.getSimpleName()));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(__instance, monsterStrings.DIALOG[0], 0.5F, 2.0F));
                ___firstTurn[0] = false;
            }
        }
    }
}
