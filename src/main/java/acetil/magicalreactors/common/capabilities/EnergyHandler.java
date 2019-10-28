package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHandler implements IEnergyStorage {
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private boolean canReceive;
    private boolean canExtract;
    private int energy;
    private boolean holdsEnergy;
    public EnergyHandler (int capacity, int maxReceive, int maxExtract, boolean canReceive, boolean canExtract) {

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.canReceive = canReceive;
        this.canExtract = canExtract;
        holdsEnergy = true;
    }
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receive = Math.min(maxReceive, this.maxReceive);
        int energyReceived = Math.min(receive, capacity - energy);
        if (!simulate && holdsEnergy) {
            energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extract = Math.min(maxExtract, this.maxExtract);
        int energyExtracted = Math.min(extract, capacity);
        if (!simulate && holdsEnergy) {
            energy -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        //System.out.println("Getting energy stored");
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        //System.out.println("Getting max energy stored");
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return canExtract;
    }

    @Override
    public boolean canReceive() {
        return canReceive;
    }

    public void setCanExtract (boolean canExtract) {
        this.canExtract = canExtract;
    }

    public void setCanReceive (boolean canReceive) {
        this.canReceive = canReceive;
    }

    public void setHoldsEnergy (boolean holdsEnergy) {
        this.holdsEnergy = holdsEnergy;
    }

    public void setMaxExtract (int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive (int maxReceive) {
        this.maxReceive = maxReceive;
    }
    public void readNBT (NBTTagCompound nbt) {
        this.energy = nbt.getInteger("stored_energy");
    }
    public NBTTagCompound writeNBT () {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("stored_energy", this.energy);
        return nbt;
    }
}
