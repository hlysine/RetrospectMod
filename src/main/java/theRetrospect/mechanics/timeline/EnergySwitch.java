package theRetrospect.mechanics.timeline;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theRetrospect.subscribers.EnergySourceChangedSubscriber;

import java.util.HashMap;
import java.util.Map;

public class EnergySwitch {

    private static EnergySource currentSource = EnergySource.NORMAL;

    private static final Map<EnergySource, EnergyState> energyStates = new HashMap<>();

    static {
        energyStates.put(EnergySource.NORMAL, new EnergyState());
        energyStates.put(EnergySource.VOLATILE, new EnergyState());
    }

    public static EnergySource getCurrentSource() {
        return currentSource;
    }

    public static void setCurrentSource(EnergySource currentSource) {
        energyStates.put(EnergySwitch.currentSource, extractState());
        EnergySwitch.currentSource = currentSource;
        restoreState(energyStates.get(currentSource));

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof EnergySourceChangedSubscriber) {
                ((EnergySourceChangedSubscriber) power).onEnergySourceChanged();
            }
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof EnergySourceChangedSubscriber) {
                ((EnergySourceChangedSubscriber) relic).onEnergySourceChanged();
            }
        }
    }

    private static void restoreState(EnergyState state) {
        AbstractDungeon.player.energy.energy = state.maxEnergy;
        EnergyPanel.setEnergy(state.currentEnergy);
    }

    private static EnergyState extractState() {
        EnergyState state = new EnergyState();
        state.currentEnergy = EnergyPanel.getCurrentEnergy();
        state.maxEnergy = AbstractDungeon.player.energy.energy;
        return state;
    }


    public enum EnergySource {
        /**
         * Normal energy used by player.
         */
        NORMAL,
        /**
         * Energy used by cards in timelines.
         */
        VOLATILE
    }

    private static class EnergyState {
        public int currentEnergy = 0;
        public int maxEnergy = 0;
    }
}
