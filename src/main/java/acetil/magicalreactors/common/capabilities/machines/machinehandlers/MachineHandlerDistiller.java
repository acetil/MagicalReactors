package acetil.magicalreactors.common.capabilities.machines.machinehandlers;

public class MachineHandlerDistiller extends MachineHandlerFluid{

    public MachineHandlerDistiller(String machine, int fluidInputSlots, int fluidOutputSlots, int energyUseRate, int bottomSlots) {
        super(machine, 0, 0, fluidInputSlots, fluidOutputSlots, energyUseRate);
    }
}
