package theRetrospect.runmods;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import theRetrospect.util.TextureLoader;

public class CustomDailyMod extends AbstractDailyMod {

    public CustomDailyMod(String setId, String name, String description, String imgUrl, boolean positive, AbstractPlayer.PlayerClass exclusion) {
        super(setId, name, description, imgUrl, positive, exclusion);
        this.img = TextureLoader.getTexture(imgUrl);
    }

    public CustomDailyMod(String setId, String name, String description, String imgUrl, boolean positive) {
        this(setId, name, description, imgUrl, positive, null);
    }
}
