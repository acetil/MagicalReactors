package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import acetil.magicalreactors.common.capabilities.reactor.ReactorEnergyInterface;

import java.util.HashMap;
import java.util.Map;

public class TileReactorInterfaceEnergy extends TileEntity implements ITickable {
    private EnergyHandler energyHandler;
    private ReactorEnergyInterface reactorInterface;
    private int capacity;
    private int energyOutputRate;
    public TileReactorInterfaceEnergy (int capacity, int energyOutputRate) {
        energyHandler = new EnergyHandler(capacity, 0, energyOutputRate, false, true);
        reactorInterface = new ReactorEnergyInterface(energyHandler);
        this.capacity = capacity;
        this.energyOutputRate = energyOutputRate;
    }
    public TileReactorInterfaceEnergy () {
        energyHandler = null;
        reactorInterface = null;
    }

    @Override
    public void update() {
        if (this.world.isRemote) {
            giveEnergy(Math.min(energyOutputRate, energyHandler.getEnergyStored()));
        }
    }
    private void giveEnergy (int energy) {
        HashMap<EnumFacing, TileEntity> tiles = new HashMap<>();
        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity te = world.getTileEntity(pos.offset(side));
            if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, side)) {
                tiles.put(side, te);
            }
        }
        if (tiles.size() <= 0) {
            return;
        }
        int energyPerSide = energy / tiles.size();
        int extraEnergy = 0;
        for (Map.Entry<EnumFacing, TileEntity> entry : tiles.entrySet()) {
            EnumFacing side = entry.getKey();
            TileEntity te = entry.getValue();
            int energyGiven = te.getCapability(CapabilityEnergy.ENERGY, side)
                    .receiveEnergy(energyPerSide, false);
            if (energyGiven < energyPerSide) {
                extraEnergy += energyPerSide - energyGiven;
            }
        }
        energyHandler.extractEnergy(energy - extraEnergy, false);
        markDirty();
    }
    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyHandler);
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return CapabilityReactorInterface.REACTOR_INTERFACE.cast(reactorInterface);
        }
        return super.getCapability(capability, facing);
    }
    @Override
    public void readFromNBT (NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("energy")) {
            NBTTagCompound energyCompound = nbt.getCompoundTag("energy");
            capacity = energyCompound.getInteger("capacity");
            energyOutputRate = energyCompound.getInteger("output_rate");
            energyHandler = new EnergyHandler(capacity, 0, energyOutputRate, false, true);
            reactorInterface = new ReactorEnergyInterface(energyHandler);
            if (nbt.hasKey("handler")) {
                energyHandler.readNBT(nbt.getCompoundTag("handler"));
            }
        }
    }
    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound energyCompound = new NBTTagCompound();
        energyCompound.setInteger("capacity", capacity);
        energyCompound.setInteger("output_rate", energyOutputRate);
        energyCompound.setTag("handler", energyHandler.writeNBT());
        nbt.setTag("energy", energyCompound);
        return nbt;
    }
}
