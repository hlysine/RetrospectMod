package theRetrospect.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicInfo {
    String IMG;
    AbstractRelic.RelicTier RARITY;

    public String getImage() {
        return IMG;
    }

    public AbstractRelic.RelicTier getRarity() {
        return RARITY;
    }
}
