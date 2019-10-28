package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidDistillHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMachineDistiller extends TileMachineBase {
    private EnumDistillState distillState = EnumDistillState.NONE;
    private TileMachineDistiller controller;
    private int bottomSlots;
    public TileMachineDistiller(String machine, int bottomSlots) {
        super(machine);
        this.bottomSlots = bottomSlots;
    }
    @Override
    public void update() {
        if (distillState == EnumDistillState.BOTTOM) {
            super.update();
        }
    }
    @Override
    public boolean hasCapability (@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (distillState == EnumDistillState.TOP && controller != null) {
            return controller.hasCapability(capability, facing == null ? null : EnumFacing.UP);
        } else if (distillState == EnumDistillState.TOP ) {
            return super.hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing)
                || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && machineFluidHandler != null)
                || (capability == CapabilityEnergy.ENERGY && energyHandler != null)
                || (capability == CapabilityMachine.MACHINE_CAPABILITY && machineHandler != null);
    }
    @Override
    public <T> T getCapability (@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (distillState == EnumDistillState.TOP && controller != null) {
            return controller.getCapability(capability, EnumFacing.UP);
        }
        if (distillState == EnumDistillState.BOTTOM) {
            if (capability == CapabilityMachine.MACHINE_CAPABILITY) {
                return CapabilityMachine.MACHINE_CAPABILITY.cast(machineHandler);
            } else if (capability == CapabilityEnergy.ENERGY) {
                return CapabilityEnergy.ENERGY.cast(energyHandler);
            } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                if (facing == null) {
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(machineFluidHandler);
                } else if (facing == EnumFacing.UP) {
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                            .cast(new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, false));
                } else {
                    return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
                            .cast(new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, true));
                }
            }
        }
        return super.getCapability(capability, facing);
    }
    public void updateDistillState (EnumDistillState state) {
        distillState = state;
    }
    public void setController (TileMachineDistiller controller) {
        this.controller = controller;
    }
    public TileMachineDistiller setBottomSlots (int bottomSlots) {
        this.bottomSlots = bottomSlots;
        return this;
    }
}
