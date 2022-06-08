package theRetrospect.patches.togetherInSpire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import spireTogether.monsters.AbstractCustomChar;
import spireTogether.monsters.CharacterEntity;
import theRetrospect.RetrospectMod;
import theRetrospect.characters.TheRetrospect;

@SpirePatch(
        cls = "spireTogether.util.SpireHelper",
        method = "abstractPlayerToCustom",
        optional = true
)
public class RetrospectSkeletonNullRefPatch {
    public static SpireReturn<CharacterEntity> Prefix(AbstractPlayer p) {
        if (p instanceof TheRetrospect) {
            RetrospectMod.logger.info("Initializing TogetherInSpire Retrospect character");
            AbstractCustomChar character = new AbstractCustomChar(p);
            character.ghostAtlasLoc = TheRetrospect.SKELETON_ATLAS;
            character.skeletonUrl = TheRetrospect.SKELETON_JSON;
            return SpireReturn.Return(character);
        }
        return SpireReturn.Continue();
    }
}
