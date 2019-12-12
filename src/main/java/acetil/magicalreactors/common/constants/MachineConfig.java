package acetil.magicalreactors.common.constants;

import acetil.magicalreactors.common.utils.ModifiableSupplier;

import java.util.function.Supplier;

public class MachineConfig {
    public String NAME;
    public Supplier<Integer> ENERGY_STORAGE;
    public Supplier<Integer> MAX_RECEIVE;
    public Supplier<Integer> ENERGY_USE_RATE;
    public int defaultEnergyStorage;
    public int defaultMaxReceive;
    public int defaultEnergyUseRate;
    public MachineConfig (String name, int defaultEnergyStorage, int defaultMaxReceive, int defaultEnergyUseRate) {
        NAME = name;
        this.defaultEnergyStorage = defaultEnergyStorage;
        this.defaultMaxReceive = defaultMaxReceive;
        this.defaultEnergyUseRate = defaultEnergyUseRate;
        ENERGY_STORAGE = () -> defaultEnergyStorage;
        MAX_RECEIVE = () -> defaultMaxReceive;
        ENERGY_USE_RATE = () -> defaultEnergyUseRate;
    }
}
