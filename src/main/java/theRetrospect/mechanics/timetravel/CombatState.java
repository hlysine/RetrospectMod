package theRetrospect.mechanics.timetravel;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theRetrospect.effects.PlayerTurnWithoutEnergyEffect;
import theRetrospect.util.CardUtils;
import theRetrospect.util.CloneUtils;

import java.util.ArrayList;

public class CombatState {

    private static final Logger logger = LogManager.getLogger(CombatState.class);

    public MonsterGroup monsters;
    public PlayerState player;

    public Random monsterRng;
    public Random mapRng;
    public Random eventRng;
    public Random merchantRng;
    public Random cardRng;
    public Random treasureRng;
    public Random relicRng;
    public Random potionRng;
    public Random monsterHpRng;
    public Random aiRng;
    public Random shuffleRng;
    public Random cardRandomRng;
    public Random miscRng;

    public ArrayList<AbstractCard> cardsPlayedThisTurn;
    public ArrayList<AbstractCard> cardsManuallyPlayedThisTurn;
    public ArrayList<AbstractOrb> orbsChanneledThisTurn;
    public int mantraGained;
    public int totalDiscardedThisTurn = 0;
    public int damageReceivedThisTurn = 0;
    public int playerHpLastTurn;
    public int turn = 0;

    private CombatState() {
    }

    public static CombatState extractState() {
        logger.info("Start extracting combat state");
        long startTime = System.currentTimeMillis();

        CombatState state = new CombatState();

        state.monsters = CloneUtils.cloneMonsterGroup(AbstractDungeon.getMonsters());
        state.player = PlayerState.extractState(AbstractDungeon.player);

        state.monsterRng = AbstractDungeon.monsterRng.copy();
        state.mapRng = AbstractDungeon.mapRng.copy();
        state.eventRng = AbstractDungeon.eventRng.copy();
        state.merchantRng = AbstractDungeon.merchantRng.copy();
        state.cardRng = AbstractDungeon.cardRng.copy();
        state.treasureRng = AbstractDungeon.treasureRng.copy();
        state.relicRng = AbstractDungeon.relicRng.copy();
        state.potionRng = AbstractDungeon.potionRng.copy();
        state.monsterHpRng = AbstractDungeon.monsterHpRng.copy();
        state.aiRng = AbstractDungeon.aiRng.copy();
        state.shuffleRng = AbstractDungeon.shuffleRng.copy();
        state.cardRandomRng = AbstractDungeon.cardRandomRng.copy();
        state.miscRng = AbstractDungeon.miscRng.copy();

        state.cardsPlayedThisTurn = CloneUtils.cloneCardList(AbstractDungeon.actionManager.cardsPlayedThisTurn);
        state.cardsManuallyPlayedThisTurn = CloneUtils.cloneCardList(CardUtils.cardsManuallyPlayedThisTurn);
        state.orbsChanneledThisTurn = new ArrayList<>(AbstractDungeon.actionManager.orbsChanneledThisTurn);
        state.mantraGained = AbstractDungeon.actionManager.mantraGained;
        state.totalDiscardedThisTurn = GameActionManager.totalDiscardedThisTurn;
        state.damageReceivedThisTurn = GameActionManager.damageReceivedThisTurn;
        state.playerHpLastTurn = GameActionManager.playerHpLastTurn;
        state.turn = GameActionManager.turn;

        logger.info("Extracting combat state took " + (System.currentTimeMillis() - startTime) + "ms");
        return state;
    }

    public void restoreStateForRewind() {
        AbstractDungeon.getCurrRoom().monsters = CloneUtils.cloneMonsterGroup(this.monsters);
        this.player.restoreStateForRewind(AbstractDungeon.player);

        AbstractDungeon.monsterRng = this.monsterRng.copy();
        AbstractDungeon.monsterHpRng = this.monsterHpRng.copy();
        AbstractDungeon.aiRng = this.aiRng.copy();
        AbstractDungeon.miscRng = this.miscRng.copy();

        AbstractDungeon.actionManager.cardsPlayedThisTurn = CloneUtils.cloneCardList(this.cardsPlayedThisTurn);
        CardUtils.cardsManuallyPlayedThisTurn = CloneUtils.cloneCardList(this.cardsManuallyPlayedThisTurn);
        AbstractDungeon.actionManager.orbsChanneledThisTurn = new ArrayList<>(this.orbsChanneledThisTurn);
        AbstractDungeon.actionManager.mantraGained = this.mantraGained;
        GameActionManager.totalDiscardedThisTurn = this.totalDiscardedThisTurn;
        GameActionManager.damageReceivedThisTurn = this.damageReceivedThisTurn;
        GameActionManager.playerHpLastTurn = this.playerHpLastTurn;
        GameActionManager.turn = this.turn;

        AbstractDungeon.topLevelEffects.add(new PlayerTurnWithoutEnergyEffect());
    }

    public void restoreStateCompletely() {
        AbstractDungeon.getCurrRoom().monsters = CloneUtils.cloneMonsterGroup(this.monsters);
        this.player.restoreStateCompletely(AbstractDungeon.player);

        AbstractDungeon.monsterRng = this.monsterRng.copy();
        AbstractDungeon.mapRng = this.mapRng.copy();
        AbstractDungeon.eventRng = this.eventRng.copy();
        AbstractDungeon.merchantRng = this.merchantRng.copy();
        AbstractDungeon.cardRng = this.cardRng.copy();
        AbstractDungeon.treasureRng = this.treasureRng.copy();
        AbstractDungeon.relicRng = this.relicRng.copy();
        AbstractDungeon.potionRng = this.potionRng.copy();
        AbstractDungeon.monsterHpRng = this.monsterHpRng.copy();
        AbstractDungeon.aiRng = this.aiRng.copy();
        AbstractDungeon.shuffleRng = this.shuffleRng.copy();
        AbstractDungeon.cardRandomRng = this.cardRandomRng.copy();
        AbstractDungeon.miscRng = this.miscRng.copy();

        AbstractDungeon.actionManager.cardsPlayedThisTurn = CloneUtils.cloneCardList(this.cardsPlayedThisTurn);
        CardUtils.cardsManuallyPlayedThisTurn = CloneUtils.cloneCardList(this.cardsManuallyPlayedThisTurn);
        AbstractDungeon.actionManager.orbsChanneledThisTurn = new ArrayList<>(this.orbsChanneledThisTurn);
        AbstractDungeon.actionManager.mantraGained = this.mantraGained;
        GameActionManager.totalDiscardedThisTurn = this.totalDiscardedThisTurn;
        GameActionManager.damageReceivedThisTurn = this.damageReceivedThisTurn;
        GameActionManager.playerHpLastTurn = this.playerHpLastTurn;
        GameActionManager.turn = this.turn;

        AbstractDungeon.topLevelEffects.add(new PlayerTurnWithoutEnergyEffect());
    }

    public CombatState copy() {
        CombatState state = new CombatState();

        state.monsters = CloneUtils.cloneMonsterGroup(this.monsters);
        state.player = this.player.copy();

        state.monsterRng = this.monsterRng.copy();
        state.mapRng = this.mapRng.copy();
        state.eventRng = this.eventRng.copy();
        state.merchantRng = this.merchantRng.copy();
        state.cardRng = this.cardRng.copy();
        state.treasureRng = this.treasureRng.copy();
        state.relicRng = this.relicRng.copy();
        state.potionRng = this.potionRng.copy();
        state.monsterHpRng = this.monsterHpRng.copy();
        state.aiRng = this.aiRng.copy();
        state.shuffleRng = this.shuffleRng.copy();
        state.cardRandomRng = this.cardRandomRng.copy();
        state.miscRng = this.miscRng.copy();

        state.cardsPlayedThisTurn = CloneUtils.cloneCardList(this.cardsPlayedThisTurn);
        state.cardsManuallyPlayedThisTurn = CloneUtils.cloneCardList(this.cardsManuallyPlayedThisTurn);
        state.orbsChanneledThisTurn = new ArrayList<>(this.orbsChanneledThisTurn);
        state.mantraGained = this.mantraGained;
        state.totalDiscardedThisTurn = this.totalDiscardedThisTurn;
        state.damageReceivedThisTurn = this.damageReceivedThisTurn;
        state.playerHpLastTurn = this.playerHpLastTurn;
        state.turn = this.turn;

        return state;
    }
}
