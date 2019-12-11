package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.client.gui.json.MachineGuiManager;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerBase;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerFluid;
import acetil.magicalreactors.common.constants.MachineConfig;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class MachineRegistryItem {
    public String machine;
    int inputSlots;
    int outputSlots;
    Supplier<Integer> energyCapacity;
    Supplier<Integer> maxReceive;
    Supplier<Integer> energyUseRate;
    MachineConfig config;
    boolean holdsFluid;
    int fluidInputSlots;
    int fluidOutputSlots;
    int fluidCapacity;
    int guiId;
    public Callable<IMachineCapability> factory;
    public int getInputSlots () {
        return inputSlots;
    }
    public int getOutputSlots () {
        return outputSlots;
    }
    public int getEnergyCapacity () {
        return energyCapacity.get();
    }
    public int getMaxReceive () {
        return maxReceive.get();
    }
    public int getEnergyUseRate () {
        return energyUseRate.get();
    }
    public boolean getHoldsFluid () {
        return holdsFluid;
    }
    public int getFluidInputSlots () {
        return fluidInputSlots;
    }
    public int getFluidOutputSlots () {
        return fluidOutputSlots;
    }
    public int getFluidCapacity () {
        return fluidCapacity;
    }
    public static class Builder {
        String machine;
        MachineConfig config;
        int inputSlots;
        int outputSlots;
        boolean holdsFluid;
        int fluidInputSlots;
        int fluidOutputSlots;
        int fluidCapacity;
        public Builder (String machine, int defaultEnergyCapacity, int defaultMaxReceive, int defaultEnergyUseRate) {
            this.machine = machine;
            config = new MachineConfig(machine, defaultEnergyCapacity, defaultMaxReceive, defaultEnergyUseRate);
            inputSlots = 0;
            outputSlots = 0;
            holdsFluid = false;
            fluidInputSlots = 0;
            fluidOutputSlots = 0;
        }
        public Builder setInputSlots (int inputSlots) {
            this.inputSlots = inputSlots;
            return this;
        }
        public Builder setOutputSlots (int outputSlots) {
            this.outputSlots = outputSlots;
            return this;
        }
        public Builder setFluidInputSlots (int fluidInputSlots) {
            holdsFluid = true;
            this.fluidInputSlots = fluidInputSlots;
            return this;
        }
        public Builder setFluidOutputSlots (int fluidOutputSlots) {
            holdsFluid = true;
            this.fluidOutputSlots = fluidOutputSlots;
            return this;
        }
        public Builder setFluidCapacity (int capacity) {
            holdsFluid = true;
            this.fluidCapacity = capacity;
            return this;
        }
        public MachineRegistryItem build (Callable<IMachineCapability> callable) {
            MachineRegistryItem item = new MachineRegistryItem();
            item.machine = machine;
            item.config = config;
            item.energyCapacity = config.ENERGY_STORAGE;
            item.maxReceive = config.MAX_RECEIVE;
            item.energyUseRate = config.ENERGY_USE_RATE;
            item.inputSlots = inputSlots;
            item.outputSlots = outputSlots;
            item.holdsFluid = holdsFluid;
            item.fluidInputSlots = fluidInputSlots;
            item.fluidOutputSlots = fluidOutputSlots;
            item.fluidCapacity = fluidCapacity;
            item.factory = callable;
            return item;
        }
        public MachineRegistryItem build () {
            if (!holdsFluid) {
                return this.build(() -> new MachineHandlerBase(machine, config.ENERGY_USE_RATE, inputSlots, outputSlots));
            } else {
                return this.build(() -> new MachineHandlerFluid(machine, inputSlots, outputSlots, fluidInputSlots, fluidOutputSlots, config.ENERGY_USE_RATE));
            }
        }
    }
}
