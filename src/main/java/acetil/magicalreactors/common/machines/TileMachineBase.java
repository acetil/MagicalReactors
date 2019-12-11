package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineGuiItemHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineSidedInventoryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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

public class TileMachineBase extends TileEntity implements ITickableTileEntity {
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
    public TileMachineBase () {
        this(MachineBlocks.MACHINE_BASE);
    }
    public TileMachineBase (String machine) {
        this(machine, MachineBlocks.MACHINE_BASE);
    }
    public TileMachineBase(String machine, TileEntityType<?> type) {
        super(MachineBlocks.MACHINE_BASE);
        this.machine = machine;
        initHandlers(MachineRegistry.getMachine(machine));
    }
    public TileMachineBase (TileEntityType<?> type) {
        super(type);
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
                    TileMachineBase.this.markDirty();
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
                TileMachineBase.this.markDirty();
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
    @Override
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
    }
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
}
