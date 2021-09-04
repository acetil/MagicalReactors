package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineGuiItemHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineSidedInventoryHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMachineBase extends BlockEntity {
    ItemStackHandler itemHandler;
    EnergyHandler energyHandler;
    IMachineCapability machineHandler;
    MachineFluidHandler machineFluidHandler;
    protected LazyOptional<IEnergyStorage> energyOptional = LazyOptional.empty();
    protected LazyOptional<IItemHandler> guiItemOptional = LazyOptional.empty();
    protected LazyOptional<IItemHandler> sidedItemOptional = LazyOptional.empty();
    protected LazyOptional<IMachineCapability> machineOptional = LazyOptional.empty();
    protected LazyOptional<IFluidHandler> fluidOptional = LazyOptional.empty();
    private String machine;
    private int guiId = 2;
    private int pastEnergyTickRate = 0;
    protected static final double TRACKING_RANGE = 30;
    private BlockPos pos;
    public TileMachineBase (BlockPos pos, BlockState state) {
        this(MachineBlocks.MACHINE_BASE.get(), pos, state);
    }
    public TileMachineBase (String machine, BlockPos pos, BlockState state) {
        this(machine, MachineBlocks.MACHINE_BASE.get(), pos, state);
    }
    public TileMachineBase(String machine, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.pos = pos;
        this.machine = machine;
        initHandlers(MachineRegistry.getMachine(machine));
    }
    public TileMachineBase (BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.pos = pos;
        itemHandler = null;
        energyHandler = null;
        machineHandler = null;
        machineFluidHandler = null;
    }
    protected void initHandlers (MachineRegistryItem machineRegistry) {

        energyHandler = new EnergyHandler(machineRegistry.energyCapacity, machineRegistry.maxReceive,
                machineRegistry.energyUseRate, true, false);
        energyOptional = LazyOptional.of(() -> energyHandler);
        if (machineRegistry.holdsFluid) {
            machineFluidHandler = new MachineFluidHandler(machineRegistry.fluidInputSlots, machineRegistry.fluidOutputSlots,
                    machineRegistry.fluidCapacity) {
                @Override
                protected void onContentsChanged(int slot, int amount) {
                    TileMachineBase.this.setChanged();
                    if (amount != 0) {
                        machineHandler.updateItems(itemHandler, this);

                    }
                }
            };
            fluidOptional = LazyOptional.of(() -> machineFluidHandler);
        }
        itemHandler = new ItemStackHandler(machineRegistry.inputSlots + machineRegistry.outputSlots) {
            @Override
            protected void onContentsChanged (int slot) {
                TileMachineBase.this.setChanged();
                machineHandler.updateItems(this, machineFluidHandler);
            }
        };
        guiItemOptional = LazyOptional.of(() -> new MachineGuiItemHandler(machineRegistry.machine, itemHandler,
                machineRegistry.inputSlots));
        sidedItemOptional = LazyOptional.of(() -> new MachineSidedInventoryHandler(machineRegistry.machine,
                itemHandler, machineRegistry.inputSlots));
        try {
            machineHandler = machineRegistry.factory.call();
            machineOptional = LazyOptional.of(() -> machineHandler);
        } catch (Exception e) {
            MagicalReactors.LOGGER.error("Machine \"" + machine + "\" handler factory caused an exception !");
            MagicalReactors.LOGGER.error(e);
        }
        guiId = machineRegistry.guiId;
    }

    public void tickServer () {
        if (machineHandler.isOn()) {
            var energyUsed = machineHandler.addEnergy(Math.min(machineHandler.getEnergyUseRate(), energyHandler.getEnergyStored()));
            energyHandler.extractEnergy(energyUsed, false);
            if (machineHandler.shouldUpdateWork()) {
                machineHandler.updateWork(itemHandler, machineFluidHandler);
                this.setChanged();
            }
        }
        machineHandler.sync(level, pos);
    }

    public void tickClient () {
        machineHandler.updateClient();
        energyHandler.syncClient();
    }
    /*@Override
    public void tick () {
        if (world.isRemote) {
            machineHandler.updateClient();
            energyHandler.syncClient();
            return;
        }
        if (machineHandler.isOn()) {
            //System.out.println("Machine handler is on!");
            int energyUsed = machineHandler.addEnergy(Math.min(machineHandler.getEnergyUseRate(), energyHandler.getEnergyStored()));
            energyHandler.extractEnergy(energyUsed, false);

            if (machineHandler.shouldUpdateWork()) {
                machineHandler.updateWork(itemHandler, machineFluidHandler);
                TileMachineBase.this.markDirty();
            }

        }
        machineHandler.sync(world, pos);
        energyHandler.sync(world, pos);
    }*/

    /*@Override
    public BlockPos getPos () {
        return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return guiItemOptional.cast();
            } else {
                return sidedItemOptional.cast();
            }
        } else if (capability == CapabilityEnergy.ENERGY) {
            return energyOptional.cast();
        } else if (capability == CapabilityMachine.MACHINE_CAPABILITY) {
            return machineOptional.cast();
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidOptional.cast();
        } else {
            return super.getCapability(capability, facing);
        }
    }
    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("machine_name") && itemHandler == null && energyHandler == null && machineHandler == null) {
            this.machine = nbt.getString("machine_name");
            initHandlers(MachineRegistry.getMachine(nbt.getString("machine_name")));
        }
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
        if (nbt.contains("energy")) {
            // TODO: match others by reading the child tag
            energyHandler.readNBT(nbt);
        }
        if (nbt.contains("machine")) {
            // TODO: change to have its own method (matching others)
            CapabilityMachine.MACHINE_CAPABILITY.readNBT(machineHandler, null, nbt.getCompound("machine"));
            machineHandler.updateItems(itemHandler, machineFluidHandler);
        }
        if (nbt.contains("fluids")) {
            machineFluidHandler.readNBT(nbt.getCompound("fluids"));
        }
    }
    @Override
    @Nonnull
    public CompoundNBT write (CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("items", itemHandler.serializeNBT());
        nbt.put("energy", energyHandler.writeNBT());
        nbt.put("machine", CapabilityMachine.MACHINE_CAPABILITY.writeNBT(machineHandler, null));
        if (machineFluidHandler != null) {
            nbt.put("fluids", machineFluidHandler.writeNBT());
        }
        nbt.putString("machine_name", machine);
        return nbt;
    }
    public boolean canInteractWith (PlayerEntity player) {
        return player.getDistanceSq(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f) <= 64D;
    }*/
    public void setPoweredState (boolean powered) {
        if (machineHandler != null) {
            machineHandler.updateRedstone(powered);
        }
    }
    public int getGuiId () {
        return guiId;
    }
    public String getMachine () {
        return machine;
    }
    public ItemStackHandler getItemHandler () {
        return itemHandler;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("machine_name") && itemHandler == null && energyHandler == null && machineHandler == null) {
            this.machine = nbt.getString("machine_name");
            initHandlers(MachineRegistry.getMachine(nbt.getString("machine_name")));
        }
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
        if (nbt.contains("energy")) {
            // TODO: match others by reading the child tag
            energyHandler.readNBT(nbt);
        }
        if (nbt.contains("machine")) {
            // TODO: change to have its own method (matching others)
            //CapabilityMachine.MACHINE_CAPABILITY.readNBT(machineHandler, null, nbt.getCompound("machine"));
            machineHandler.readNBT(nbt.getCompound("machine"));
            machineHandler.updateItems(itemHandler, machineFluidHandler);
        }
        if (nbt.contains("fluids")) {
            machineFluidHandler.readNBT(nbt.getCompound("fluids"));
        }
    }

    @Override
    public CompoundTag serializeNBT () {
        var nbt =  super.serializeNBT();
        nbt.put("items", itemHandler.serializeNBT());
        nbt.put("energy", energyHandler.writeNBT());
        //nbt.put("machine", CapabilityMachine.MACHINE_CAPABILITY.writeNBT(machineHandler, null));
        nbt.put("machine", machineHandler.writeNBT());
        if (machineFluidHandler != null) {
            nbt.put("fluids", machineFluidHandler.writeNBT());
        }
        nbt.putString("machine_name", machine);
        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability (@Nonnull Capability<T> capability, Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return guiItemOptional.cast();
            } else {
                return sidedItemOptional.cast();
            }
        } else if (capability == CapabilityEnergy.ENERGY) {
            return energyOptional.cast();
        } else if (capability == CapabilityMachine.MACHINE_CAPABILITY) {
            return machineOptional.cast();
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidOptional.cast();
        } else {
            return super.getCapability(capability, facing);
        }
    }


}
