package theRetrospect.util;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import theRetrospect.RetrospectMod;

public class TimelineCardGlow extends CardBorderGlowManager.GlowInfo {
    @Override
    public boolean test(AbstractCard card) {
        return card.target == TimelineTargeting.TIMELINE && TimelineUtils.getTimelines(AbstractDungeon.player).size() > 0;
    }

    @Override
    public Color getColor(AbstractCard card) {
        return CardHelper.getColor(122, 67, 255);
    }

    @Override
    public String glowID() {
        return RetrospectMod.makeID(TimelineCardGlow.class.getSimpleName());
    }
}
