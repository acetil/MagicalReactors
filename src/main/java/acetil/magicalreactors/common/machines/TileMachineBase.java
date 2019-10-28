package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import acetil.magicalreactors.common.capabilities.machines.MachineFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.MagicalReactors;
import nuclear.common.capabilities.machines.*;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;
import acetil.magicalreactors.common.network.MessageMachineUpdate;
import acetil.magicalreactors.common.network.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMachineBase extends TileEntity implements ITickable {
    ItemStackHandler itemHandler;
    EnergyHandler energyHandler;
    IMachineCapability machineHandler;
    MachineFluidHandler machineFluidHandler;
    private String machine;
    private int guiId = 2;
    private int pastEnergyTickRate = 0;
    protected static final double TRACKING_RANGE = 30;
    public TileMachineBase () {
        itemHandler = null;
        energyHandler = null;
        machineHandler = null;
        machineFluidHandler = null;
    }
    public TileMachineBase(String machine) {
        this.machine = machine;
        initHandlers(MachineRegistry.getMachine(machine));
    }
    public void initHandlers (MachineRegistryItem machineRegistry) {

        energyHandler = new EnergyHandler(machineRegistry.energyCapacity, machineRegistry.maxReceive,
                machineRegistry.energyUseRate, true, false);
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
        }
        itemHandler = new ItemStackHandler(machineRegistry.inputSlots + machineRegistry.outputSlots) {
            @Override
            protected void onContentsChanged (int slot) {
                TileMachineBase.this.markDirty();
                machineHandler.updateItems(this, machineFluidHandler);
            }
        };
        try {
            machineHandler = machineRegistry.factory.call();
        } catch (Exception e) {
            MagicalReactors.LOGGER.error("Machine \"" + machine + "\" handler factory caused an exception !");
            MagicalReactors.LOGGER.error(e);
        }
        guiId = machineRegistry.guiId;
    }
    public void update () {
        if (world.isRemote) {
            machineHandler.updateClient();
            return;
        }
        if (machineHandler.isOn()) {
            //System.out.println("Machine handler is on!");
            int energyUsed = machineHandler.addEnergy(Math.min(machineHandler.getEnergyUseRate(), energyHandler.getEnergyStored()));
            energyHandler.extractEnergy(energyUsed, false);
            if (machineHandler.shouldUpdateWork()) {
                machineHandler.updateWork(itemHandler, machineFluidHandler);
                TileMachineBase.this.markDirty();
                sendMessage();
            } else if (energyUsed != pastEnergyTickRate) {
                sendMessage();
            }

        }
    }
    public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || capability == CapabilityMachine.MACHINE_CAPABILITY
                || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && machineFluidHandler != null)
                || super.hasCapability(capability, facing);
    }
    public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyHandler);
        } else if (capability == CapabilityMachine.MACHINE_CAPABILITY) {
            return CapabilityMachine.MACHINE_CAPABILITY.cast(machineHandler);
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && machineFluidHandler != null) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(machineFluidHandler);
        } else {
            return super.getCapability(capability, facing);
        }
    }
    @Override
    public void readFromNBT (NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("machine_name") && itemHandler == null && energyHandler == null && machineHandler == null) {
            this.machine = nbt.getString("machine_name");
            initHandlers(MachineRegistry.getMachine(nbt.getString("machine_name")));
        }
        if (nbt.hasKey("items")) {
            itemHandler.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
        }
        if (nbt.hasKey("energy")) {
            // TODO: match others by reading the child tag
            energyHandler.readNBT(nbt);
        }
        if (nbt.hasKey("machine")) {
            // TODO: change to have its own method (matching others)
            CapabilityMachine.MACHINE_CAPABILITY.readNBT(machineHandler, null, nbt.getTag("machine"));
            machineHandler.updateItems(itemHandler, machineFluidHandler);
        }
        if (nbt.hasKey("fluids")) {
            machineFluidHandler.readNBT(nbt.getCompoundTag("fluids"));
        }
    }
    @Override
    @Nonnull
    public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("items", itemHandler.serializeNBT());
        nbt.setTag("energy", energyHandler.writeNBT());
        nbt.setTag("machine", CapabilityMachine.MACHINE_CAPABILITY.writeNBT(machineHandler, null));
        if (machineFluidHandler != null) {
            nbt.setTag("fluids", machineFluidHandler.writeNBT());
        }
        nbt.setString("machine_name", machine);
        return nbt;
    }
    public boolean canInteractWith (EntityPlayer player) {
        return !isInvalid() && player.getDistanceSq(pos.add(0.5f, 0.5f, 0.5f)) <= 64D;
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
    private void sendMessage () {
        PacketHandler.INSTANCE.sendToAllTracking(new MessageMachineUpdate(pos.getX(), pos.getY(), pos.getZ(),
                        machineHandler.isOn(), machineHandler.getEnergyPerTick(),
                        machineHandler.energyToCompletion(), machineHandler.energyRequired()),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), TRACKING_RANGE));
    }
}
