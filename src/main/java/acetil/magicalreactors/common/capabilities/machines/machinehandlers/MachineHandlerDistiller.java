package acetil.magicalreactors.common.capabilities.machines.machinehandlers;

import java.util.function.Supplier;

public class MachineHandlerDistiller extends MachineHandlerFluid {

    public MachineHandlerDistiller(String machine, int fluidInputSlots, int fluidOutputSlots, Supplier<Integer> energyUseRate, int bottomSlots) {
        super(machine, 0, 0, fluidInputSlots, fluidOutputSlots, energyUseRate);
    }
}
