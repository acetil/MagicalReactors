package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
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
    public TileMachineDistiller(String machine, int bottomSlots, BlockPos pos, BlockState state) {
        super(machine, MachineBlocks.MACHINE_DISTILLER.get(), pos, state);
        this.bottomSlots = bottomSlots;
    }
    public TileMachineDistiller (BlockPos pos, BlockState state) {
        super(MachineBlocks.MACHINE_DISTILLER.get(), pos, state);
    }
    @Override
    protected void initHandlers (MachineRegistryItem registryItem) {
        super.initHandlers(registryItem);
        bottomOptional = LazyOptional.of(() -> new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, true));
        topOptional  = LazyOptional.of(() -> new MachineFluidDistillHandler(machineFluidHandler, bottomSlots, true));
    }
    @Override
    public void tickServer() {
        if (distillState == EnumDistillState.BOTTOM) {
            super.tickServer();
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
    /*@Override
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
    }*/

    @Override
    public CompoundTag serializeNBT () {
        var nbt =  super.serializeNBT();
        nbt.putInt("bottom_slots", bottomSlots);
        return nbt;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("bottom_slots")) {
            setBottomSlots(nbt.getInt("bottom_slots"));
        }
    }
}
