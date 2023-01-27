package theRetrospect.icons;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import theRetrospect.RetrospectMod;
import theRetrospect.util.TextureLoader;

import java.util.Collections;
import java.util.List;

public class VolatileEnergyIcon extends AbstractCustomIcon {
    public static final String ID = RetrospectMod.makeID("VolatileEnergy");
    public static final List<String> KEYWORDS = Collections.singletonList(RetrospectMod.makeID("volatile_energy").toLowerCase());
    private static VolatileEnergyIcon singleton;

    public VolatileEnergyIcon() {
        super(ID, TextureLoader.getTexture(RetrospectMod.makePowerPath("placeholder_power84.png")));
    }

    public static VolatileEnergyIcon get() {
        if (singleton == null) {
            singleton = new VolatileEnergyIcon();
        }
        return singleton;
    }

    @Override
    public List<String> keywordLinks() {
        return KEYWORDS;
    }
}
