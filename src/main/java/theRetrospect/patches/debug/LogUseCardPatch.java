package theRetrospect.patches.debug;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import theRetrospect.RetrospectMod;
import theRetrospect.util.CardUtils;

@SpirePatch(
        clz = UseCardAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCard.class,
                AbstractCreature.class
        }
)
public class LogUseCardPatch {
    @SpireInsertPatch(
            rloc = 16
    )
    public static void Insert(UseCardAction __instance, AbstractCard ___targetCard) {
        RetrospectMod.logger.info("Play card: " + ___targetCard.cardID + " source: " + CardUtils.getPlaySource(___targetCard).toString());
    }
}