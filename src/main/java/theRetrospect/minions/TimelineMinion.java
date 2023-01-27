package theRetrospect.minions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theRetrospect.RetrospectMod;
import theRetrospect.actions.general.RunnableAction;
import theRetrospect.actions.timeline.CollapseTimelineAction;
import theRetrospect.effects.TimelineAuraEffect;
import theRetrospect.powers.TimerPower;
import theRetrospect.subscribers.StateChangeSubscriber;
import theRetrospect.mechanics.timetravel.CombatStateTree;
import theRetrospect.mechanics.timetravel.StateManager;
import theRetrospect.mechanics.card.CardPlaySource;
import theRetrospect.util.CardUtils;
import theRetrospect.util.CloneUtils;

import java.util.ArrayList;

public class TimelineMinion extends AbstractMinionWithCards implements StateChangeSubscriber {

    public static final String ID = RetrospectMod.makeID(TimelineMinion.class.getSimpleName());
    private static final CharacterStrings minionStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String NAME = minionStrings.NAMES[0];
    public static final String[] TEXT = minionStrings.TEXT;

    private float auraEffectTimer = 0.5f;
    private PowerTip timelineTip;

    /**
     * If this timeline is in turn, an effect will be shown to indicate that this timeline will play cards.
     */
    public boolean inTurn = false;

    public CombatStateTree.LinearPath timelinePath;

    public TimelineMinion(AbstractPlayer summoner, int offsetX, int offsetY, CombatStateTree.LinearPath timelinePath) {
        super(NAME, ID, AbstractDungeon.player.maxHealth, 0, 0, 120, 140, null, offsetX, offsetY);

        this.scale = 0.5f;

        this.loadAnimation(CloneUtils.cloneAnimation(summoner, this.scale));
        CloneUtils.cloneAnimationStates(summoner, this);
        if (summoner.img != null && summoner.img.getTextureData() != null)
            this.img = new Texture(summoner.img.getTextureData());
        if (summoner.corpseImg != null && summoner.corpseImg.getTextureData() != null)
            this.corpseImg = new Texture(summoner.corpseImg.getTextureData());

        this.addPower(new TimerPower(this));
        this.powers.forEach(AbstractPower::onInitialApplication);

        this.timelinePath = timelinePath;
        applyStateForRound(StateManager.getActiveRound());
        addToBot(new RunnableAction(() -> applyStateForRound(StateManager.getActiveRound())));
    }

    @Override
    public void onActiveNodeChanged() {
        addToBot(new RunnableAction(() -> applyStateForRound(StateManager.getActiveRound())));
    }

    public void applyStateForRound(int round) {
        CombatStateTree.Node node = timelinePath.getNodeForRound(round);
        if (node == null || node.round < timelinePath.originNode.round) {
            halfDead = true;
            int start = timelinePath.originNode.round;
            int end = timelinePath.targetNode.round;
            if (start == end) {
                timelineTip = new PowerTip(NAME, TEXT[0] + start + TEXT[1]);
            } else {
                timelineTip = new PowerTip(NAME, TEXT[2] + timelinePath.originNode.round + TEXT[3] + timelinePath.targetNode.round + TEXT[4]);
            }

            setCards(new ArrayList<>());
            setCardIntents(new ArrayList<>());

            hideHealthBar();

            Color c = this.tint.color.cpy();
            c.a = 0.5f;
            this.tint.changeColor(c, 5f);

            if (round > timelinePath.targetNode.round) {
                addToBot(new CollapseTimelineAction(this));
            }
        } else {
            halfDead = false;
            timelineTip = new PowerTip(NAME, TEXT[5] + timelinePath.targetNode.round + TEXT[6]);

            setCards(CloneUtils.cloneCardList(node.cardsPlayedManually));
            setCardIntents(this.cards);

            showHealthBar();

            Color c = this.tint.color.cpy();
            c.a = 1f;
            this.tint.changeColor(c, 5f);

            this.currentHealth = node.baseState.player.currentHealth;
            this.maxHealth = node.baseState.player.maxHealth;
            this.currentBlock = node.baseState.player.currentBlock;
            TempHPField.tempHp.set(this, node.baseState.player.tempHp);
        }
    }

    @Override
    public void renderCustomTips(SpriteBatch sb, ArrayList<PowerTip> tips) {
        super.renderCustomTips(sb, tips);
        if (timelineTip != null) {
            tips.add(timelineTip);
        }
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
