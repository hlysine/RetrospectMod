package theRetrospect.minions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.effects.TimelineAuraEffect;
import theRetrospect.powers.TimerPower;

import java.util.List;

public class TimelineMinion extends AbstractMinionWithCards {

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String IMG = "theRetrospectResources/images/char/retrospectCharacter/Spriter/object1.png";
    public static final String NAME = minionStrings.NAMES[0];

    private float auraEffectTimer = 0.5f;

    public TimelineMinion(List<AbstractCard> cards, int offsetX, int offsetY, int maxHealth) {
        super(NAME, ID, maxHealth, 0, 0, 120, 120, IMG, offsetX, offsetY);
        setCards(cards);
        addPower(new TimerPower(this, 10));
        this.powers.forEach(AbstractPower::onInitialApplication);
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();
        this.auraEffectTimer -= Gdx.graphics.getDeltaTime();
        if (this.auraEffectTimer < 0.0F) {
            this.auraEffectTimer = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new TimelineAuraEffect(this));
        }
    }
}
