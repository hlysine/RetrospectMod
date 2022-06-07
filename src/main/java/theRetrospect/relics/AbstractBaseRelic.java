package theRetrospect.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

public abstract class AbstractBaseRelic extends CustomRelic {
    public AbstractBaseRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
    }

    public AbstractBaseRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
    }

    public AbstractBaseRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }
}
