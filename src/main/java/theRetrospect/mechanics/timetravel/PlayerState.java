package theRetrospect.mechanics.timetravel;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.util.CloneUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PlayerState {
    public ArrayList<AbstractPower> powers;

    public int lastDamageTaken;
    public int currentHealth;
    public int maxHealth;
    public int currentBlock;
    public int tempHp;

    public int energyManagerEnergy;
    public int energyManagerEnergyMaster;
    public int currentEnergy;

    public ArrayList<AbstractOrb> orbs;
    public int maxOrbs;

    public ArrayList<AbstractRelic> relics;

    public ArrayList<AbstractPotion> potions;
    public int potionSlots;

    public AbstractStance stance;

    public int cardsPlayedThisTurn;

    public CardGroup drawPile = new CardGroup(CardGroup.CardGroupType.DRAW_PILE);
    public CardGroup hand = new CardGroup(CardGroup.CardGroupType.HAND);
    public CardGroup discardPile = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE);
    public CardGroup exhaustPile = new CardGroup(CardGroup.CardGroupType.EXHAUST_PILE);
    public CardGroup limbo = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    private PlayerState() {
    }

    public static PlayerState extractState(AbstractPlayer player) {
        PlayerState state = new PlayerState();

        state.powers = player.powers.stream().map(CloneUtils::clonePower).collect(Collectors.toCollection(ArrayList::new));

        state.lastDamageTaken = player.lastDamageTaken;
        state.currentHealth = player.currentHealth;
        state.maxHealth = player.maxHealth;
        state.currentBlock = player.currentBlock;
        state.tempHp = TempHPField.tempHp.get(player);

        state.energyManagerEnergy = player.energy.energy;
        state.energyManagerEnergyMaster = player.energy.energyMaster;
        state.currentEnergy = EnergyPanel.getCurrentEnergy();

        state.orbs = player.orbs.stream().map(CloneUtils::cloneOrb).collect(Collectors.toCollection(ArrayList::new));
        state.maxOrbs = player.maxOrbs;

        state.relics = player.relics.stream().map(CloneUtils::cloneRelic).collect(Collectors.toCollection(ArrayList::new));

        state.potions = player.potions.stream().map(CloneUtils::clonePotion).collect(Collectors.toCollection(ArrayList::new));
        state.potionSlots = player.potionSlots;

        state.stance = CloneUtils.cloneStance(player.stance);

        state.cardsPlayedThisTurn = player.cardsPlayedThisTurn;

        state.drawPile = CloneUtils.cloneCardGroup(player.drawPile);
        state.hand = CloneUtils.cloneCardGroup(player.hand);
        state.discardPile = CloneUtils.cloneCardGroup(player.discardPile);
        state.exhaustPile = CloneUtils.cloneCardGroup(player.exhaustPile);
        state.limbo = CloneUtils.cloneCardGroup(player.limbo);

        return state;
    }

    public void restoreStateForRewind(AbstractPlayer player) {
        player.currentHealth = currentHealth;
        player.maxHealth = maxHealth;
        TempHPField.tempHp.set(player, tempHp);

        player.cardsPlayedThisTurn = cardsPlayedThisTurn;

        player.healthBarUpdatedEvent();
        player.hand.refreshHandLayout();
        AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
    }

    public void restoreStateCompletely(AbstractPlayer player) {
        player.powers = powers;

        player.lastDamageTaken = lastDamageTaken;
        player.currentHealth = currentHealth;
        player.maxHealth = maxHealth;
        player.currentBlock = currentBlock;
        TempHPField.tempHp.set(player, tempHp);

        player.energy.energy = energyManagerEnergy;
        player.energy.energyMaster = energyManagerEnergyMaster;
        EnergyPanel.setEnergy(currentEnergy);

        player.orbs = orbs.stream().map(CloneUtils::cloneOrb).collect(Collectors.toCollection(ArrayList::new));
        player.maxOrbs = maxOrbs;

        player.relics = relics.stream().map(CloneUtils::cloneRelic).collect(Collectors.toCollection(ArrayList::new));

        player.potions = potions.stream().map(CloneUtils::clonePotion).collect(Collectors.toCollection(ArrayList::new));
        player.potionSlots = potionSlots;

        player.stance = CloneUtils.cloneStance(stance);

        player.cardsPlayedThisTurn = cardsPlayedThisTurn;

        player.drawPile = CloneUtils.cloneCardGroup(drawPile);
        player.hand = CloneUtils.cloneCardGroup(hand);
        player.discardPile = CloneUtils.cloneCardGroup(discardPile);
        player.exhaustPile = CloneUtils.cloneCardGroup(exhaustPile);
        player.limbo = CloneUtils.cloneCardGroup(limbo);

        player.healthBarUpdatedEvent();
        player.hand.refreshHandLayout();
        AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
    }

    private CardGroup createCardGroupWithUpdatedStats(CardGroup group) {
        CardGroup newGroup = new CardGroup(group.type);
        for (AbstractCard card : group.group) {
            HashSet<AbstractCard> updatedCard = GetAllInBattleInstances.get(card.uuid);
            AbstractCard newCard;
            if (updatedCard.isEmpty()) {
                newCard = card.makeSameInstanceOf();
            } else {
                newCard = updatedCard.iterator().next().makeSameInstanceOf();
            }
            newGroup.addToTop(newCard);
        }
        return newGroup;
    }

    public PlayerState copy() {
        PlayerState state = new PlayerState();

        state.powers = this.powers.stream().map(CloneUtils::clonePower).collect(Collectors.toCollection(ArrayList::new));

        state.lastDamageTaken = this.lastDamageTaken;
        state.currentHealth = this.currentHealth;
        state.maxHealth = this.maxHealth;
        state.currentBlock = this.currentBlock;
        state.tempHp = this.tempHp;

        state.energyManagerEnergy = this.energyManagerEnergy;
        state.energyManagerEnergyMaster = this.energyManagerEnergyMaster;
        state.currentEnergy = this.currentEnergy;

        state.orbs = this.orbs.stream().map(CloneUtils::cloneOrb).collect(Collectors.toCollection(ArrayList::new));
        state.maxOrbs = this.maxOrbs;

        state.relics = this.relics.stream().map(CloneUtils::cloneRelic).collect(Collectors.toCollection(ArrayList::new));

        state.potions = this.potions.stream().map(CloneUtils::clonePotion).collect(Collectors.toCollection(ArrayList::new));
        state.potionSlots = this.potionSlots;

        state.stance = CloneUtils.cloneStance(this.stance);

        state.cardsPlayedThisTurn = this.cardsPlayedThisTurn;

        state.drawPile = CloneUtils.cloneCardGroup(this.drawPile);
        state.hand = CloneUtils.cloneCardGroup(this.hand);
        state.discardPile = CloneUtils.cloneCardGroup(this.discardPile);
        state.exhaustPile = CloneUtils.cloneCardGroup(this.exhaustPile);
        state.limbo = CloneUtils.cloneCardGroup(this.limbo);

        return state;
    }
}
