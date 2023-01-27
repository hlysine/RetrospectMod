package theRetrospect.icons;

import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import theRetrospect.RetrospectMod;
import theRetrospect.util.TextureLoader;

import java.util.Collections;
import java.util.List;

public class UnstableEnergyIcon extends AbstractCustomIcon {
    public static final String ID = RetrospectMod.makeID("UnstableEnergy");
    public static final List<String> KEYWORDS = Collections.singletonList(RetrospectMod.makeID("unstable_energy").toLowerCase());
    private static UnstableEnergyIcon singleton;

    public UnstableEnergyIcon() {
        super(ID, TextureLoader.getTexture(RetrospectMod.makePowerPath("placeholder_power84.png")));
    }

    public static UnstableEnergyIcon get() {
        if (singleton == null) {
            singleton = new UnstableEnergyIcon();
        }
        return singleton;
    }

    @Override
    public List<String> keywordLinks() {
        return KEYWORDS;
    }
}
