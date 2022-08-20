package theRetrospect.minions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.effects.TimelineAuraEffect;
import theRetrospect.powers.TimerPower;
import theRetrospect.util.AnimationUtils;
import theRetrospect.util.CardPlaySource;
import theRetrospect.util.CardUtils;

import java.util.List;

public class TimelineMinion extends AbstractMinionWithCards {

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String NAME = minionStrings.NAMES[0];

    private float auraEffectTimer = 0.5f;

    /**
     * If this timeline is in turn, an effect will be shown to indicate that this timeline will play cards.
     */
    public boolean inTurn = false;

    public TimelineMinion(AbstractPlayer summoner, List<AbstractCard> cards, int offsetX, int offsetY, int maxHealth) {
        super(NAME, ID, maxHealth, 0, 0, 120, 140, null, offsetX, offsetY);

        this.scale = 0.5f;

        this.loadAnimation(AnimationUtils.cloneAnimation(summoner, this.scale));
        AnimationUtils.cloneAnimationStates(summoner, this);
        this.img = new Texture(summoner.img.getTextureData());
        this.corpseImg = new Texture(summoner.corpseImg.getTextureData());

        setCards(cards);
        addPower(new TimerPower(this, 1));
        this.powers.forEach(AbstractPower::onInitialApplication);
    }

    @Override
    protected void processNewCard(AbstractCard card) {
        super.processNewCard(card);
        CardUtils.setPlaySource(card, CardPlaySource.TIMELINE);
        card.beginGlowing();
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

    @Override
    public void playDeathAnimation() {
        super.playDeathAnimation();
        this.tint.color = new Color(1, 1, 1, 0.5f);
        for (AbstractCard card : cardStack.cards) {
            Color tint = ReflectionHacks.getPrivate(card, AbstractCard.class, "tintColor");
            tint.a = 0.3f;
        }
    }
}
