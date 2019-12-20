package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidDistillHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMachineDistiller extends TileMachineBase {
    private EnumDistillState distillState = EnumDistillState.NONE;
    private TileMachineDistiller controller;
    private LazyOptional<IFluidHandler> bottomOptional = LazyOptional.empty();
    private LazyOptional<IFluidHandler> topOptional = LazyOptional.empty();
    private int bottomSlots;
    public TileMachineDistiller(String machine, int bottomSlots) {
        super(machine, MachineBlocks.MACHINE_DISTILLER.get());
        this.bottomSlots = bottomSlots;
    }
    public TileMachineDistiller () {
        super(MachineBlocks.MACHINE_DISTILLER.get());
    }
    @Override
    protected void initHandlers (MachineRegistryItem registryItem) {
        super.initHandlers(registryItem);
        bottomOptional = LazyOptional.of(() -> new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, true));
        topOptional  = LazyOptional.of(() -> new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, true));
    }
    @Override
    public void tick() {
        if (distillState == EnumDistillState.BOTTOM) {
            super.tick();
        }
    }
    @Override
    public <T> LazyOptional<T> getCapability (@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (distillState == EnumDistillState.TOP && controller != null) {
            return controller.getCapability(capability, Direction.UP);
        }
        if (distillState == EnumDistillState.BOTTOM) {
            if (capability == CapabilityMachine.MACHINE_CAPABILITY) {
                return machineOptional.cast();
            } else if (capability == CapabilityEnergy.ENERGY) {
                return energyOptional.cast();
            } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                if (facing == null) {
                    return fluidOptional.cast();
                } else if (facing == Direction.UP) {
                    return topOptional.cast();
                } else {
                    return bottomOptional.cast();
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
    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("bottom_slots")) {
            setBottomSlots(nbt.getInt("bottom_slots"));
        }
    }
    @Override
    public CompoundNBT write (CompoundNBT nbt) {
        nbt.putInt("bottoms_slots", bottomSlots);
        return super.write(nbt);
    }
}
