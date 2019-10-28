package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.client.gui.json.MachineGuiManager;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerBase;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.MachineHandlerFluid;

import java.util.concurrent.Callable;

public class MachineRegistryItem {
    public String machine;
    public int inputSlots;
    public int outputSlots;
    public int energyCapacity;
    public int maxReceive;
    public int energyUseRate;
    public boolean holdsFluid;
    public int fluidInputSlots;
    public int fluidOutputSlots;
    public int fluidCapacity;
    public int guiId;
    public Callable<IMachineCapability> factory;
    public MachineRegistryItem (String machine, int inputSlots, int outputSlots, int energyCapacity, int guiId, int maxReceive, int energyUseRate) {
        this(machine, inputSlots, outputSlots, energyCapacity, guiId, maxReceive, energyUseRate,
                false, 0, 0, 0, () -> new MachineHandlerBase(machine, energyUseRate, inputSlots, outputSlots));
    }
    public MachineRegistryItem (String machine, int inputSlots, int outputSlots, int energyCapacity, int guiId, int maxReceive, int energyUseRate,
                                boolean holdsFluid, int fluidInputSlots, int fluidOutputSlots, int fluidCapacity) {
        this(machine, inputSlots, outputSlots, energyCapacity, guiId, maxReceive, energyUseRate, holdsFluid, fluidInputSlots,
                fluidOutputSlots, fluidCapacity, () -> new MachineHandlerFluid(machine, inputSlots, outputSlots, fluidInputSlots, fluidOutputSlots, energyUseRate));
    }
    public MachineRegistryItem (String machine, int inputSlots, int outputSlots, int energyCapacity, int guiId, int maxReceive, int energyUseRate,
                                boolean holdsFluid, int fluidInputSlots, int fluidOutputSlots, int fluidCapacity, Callable<IMachineCapability> factoryCallable) {
        this.machine = machine;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.energyCapacity = energyCapacity;
        this.maxReceive = maxReceive;
        this.energyUseRate = energyUseRate;
        this.holdsFluid = holdsFluid;
        this.fluidInputSlots = fluidInputSlots;
        this.fluidOutputSlots = fluidOutputSlots;
        this.fluidCapacity = fluidCapacity;
        this.factory = factoryCallable;
        this.guiId = MachineGuiManager.addGuiId(machine);
        //this.guiId = guiId;
    }
}
