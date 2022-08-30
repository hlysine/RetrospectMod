package theRetrospect.relics;

import basemod.abstracts.CustomRelic;
import hlysine.STSItemInfo.RelicInfo;
import theRetrospect.util.TextureLoader;

import static theRetrospect.RetrospectMod.*;

public abstract class AbstractBaseRelic extends CustomRelic {

    protected final RelicInfo info;

    public AbstractBaseRelic(String id, LandingSound sfx) {
        super(
                id,
                TextureLoader.getTexture(makeRelicPath(getRelicInfo(id).getImage())),
                TextureLoader.getTexture(makeRelicOutlinePath(getRelicInfo(id).getImage())),
                getRelicInfo(id).getRarity(),
                sfx
        );

        info = getRelicInfo(id);
    }
}
