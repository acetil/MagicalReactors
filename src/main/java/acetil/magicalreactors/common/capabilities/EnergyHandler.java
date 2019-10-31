package acetil.magicalreactors.common.capabilities;

import acetil.magicalreactors.common.network.MessageEnergyUpdate;
import acetil.magicalreactors.common.network.PacketHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnergyHandler implements IEnergyStorage {
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private boolean canReceive;
    private boolean canExtract;
    private int energy;
    private boolean holdsEnergy;
    private int lastEnergy;
    private int energyChange;
    private int lastEnergyChange;
    public EnergyHandler (int capacity, int maxReceive, int maxExtract, boolean canReceive, boolean canExtract) {

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.canReceive = canReceive;
        this.canExtract = canExtract;
        holdsEnergy = true;
        lastEnergy = energy;
        energyChange = 0;
        lastEnergyChange = 0;
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
    public void readNBT (CompoundNBT nbt) {
        this.energy = nbt.getInt("stored_energy");
        lastEnergy = energy;
        energyChange = 0;
        lastEnergyChange = 0;
    }
    public CompoundNBT writeNBT () {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("stored_energy", this.energy);
        return nbt;
    }
    public void setTotalEnergy (int energy) {
        this.energy = energy;
    }
    public void setEnergyChange (int energyChange) {
        this.energyChange = energyChange;
    }
    public void updateEnergyChange () {
        lastEnergyChange = energyChange;
        energyChange = energy - lastEnergy;
        lastEnergy = energy;
    }
    public void sync (World world, BlockPos pos) {
        updateEnergyChange();
        if (energyChange != lastEnergyChange) {
            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)),
                    new MessageEnergyUpdate(pos, energy, energyChange));
            System.out.println("Sending energy update!");
        }
    }
    public void syncClient () {
        energy = Math.max(0, Math.min(energy + energyChange, capacity));
    }
}
