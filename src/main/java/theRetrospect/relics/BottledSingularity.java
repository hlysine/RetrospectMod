package theRetrospect.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theRetrospect.RetrospectMod;
import theRetrospect.cards.curses.Singularity;
import theRetrospect.util.CardUtils;
import theRetrospect.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static theRetrospect.RetrospectMod.makeRelicOutlinePath;
import static theRetrospect.RetrospectMod.makeRelicPath;

public class BottledSingularity extends AbstractBaseRelic implements CustomBottleRelic, CustomSavable<List<Integer>> {

    public static int CARD_COUNT = 3;

    private static List<AbstractCard> cards = new ArrayList<>();
    private boolean cardsSelected = true;

    public static final String ID = RetrospectMod.makeID(BottledSingularity.class.getSimpleName());
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("bottled_singularity.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("bottled_singularity.png"));

    public BottledSingularity() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return CardUtils::getIsInBottledSingularity;
    }

    @Override
    public List<Integer> onSave() {
        if (cards.size() > 0) {
            List<Integer> indices = new ArrayList<>(cards.size());
            for (AbstractCard card : cards) {
                indices.add(AbstractDungeon.player.masterDeck.group.indexOf(card));
            }
            return indices;
        } else {
            return null;
        }
    }

    @Override
    public void onLoad(List<Integer> cardIndices) {
        if (cardIndices == null) {
            return;
        }
        for (Integer idx : cardIndices) {
            if (idx >= 0 && idx < AbstractDungeon.player.masterDeck.group.size()) {
                AbstractCard card = AbstractDungeon.player.masterDeck.group.get(idx);
                if (card != null) {
                    cards.add(card);
                    CardUtils.setIsInBottledSingularity(card, true);
                }
            }
        }
        setDescriptionAfterLoading();
    }

    @Override
    public void onEquip() {
        cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        AbstractDungeon.gridSelectScreen.open(group, CARD_COUNT, DESCRIPTIONS[2] + name + DESCRIPTIONS[3], false, false, false, false);
    }

    @Override
    public void onUnequip() {
        if (cards.size() > 0) {
            for (AbstractCard card : cards) {
                AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
                if (cardInDeck != null) {
                    CardUtils.setIsInBottledSingularity(cardInDeck, false);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == CARD_COUNT) {
            cardsSelected = true;
            cards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            for (AbstractCard card : cards) {
                CardUtils.setIsInBottledSingularity(card, true);
            }
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Singularity(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            UnlockTracker.markCardAsSeen(Singularity.ID);
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
