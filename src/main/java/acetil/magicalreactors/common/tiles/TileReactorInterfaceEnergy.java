package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import acetil.magicalreactors.common.capabilities.reactor.ReactorEnergyInterface;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TileReactorInterfaceEnergy extends TileEntity implements ITickableTileEntity {
    private EnergyHandler energyHandler;
    private ReactorEnergyInterface reactorInterface;
    private LazyOptional<IEnergyStorage> energyOptional = LazyOptional.empty();
    private LazyOptional<IReactorInterfaceHandler> interfaceOptional = LazyOptional.empty();
    private int capacity;
    private int energyOutputRate;
    public TileReactorInterfaceEnergy (int capacity, int energyOutputRate) {
        super(ModBlocks.ENERGY_INTERFACE);
        energyHandler = new EnergyHandler(capacity, 0, energyOutputRate, false, true);
        reactorInterface = new ReactorEnergyInterface(energyHandler);
        energyOptional = LazyOptional.of(() -> energyHandler);
        interfaceOptional = LazyOptional.of(() -> reactorInterface);
        this.capacity = capacity;
        this.energyOutputRate = energyOutputRate;
    }
    public TileReactorInterfaceEnergy () {
        super(ModBlocks.ENERGY_INTERFACE);
        energyHandler = null;
        reactorInterface = null;
    }

    @Override
    public void tick() {
        if (this.world.isRemote) {
            giveEnergy(Math.min(energyOutputRate, energyHandler.getEnergyStored()));
        }
    }
    private void giveEnergy (int energy) {
        HashMap<Direction, TileEntity> tiles = new HashMap<>();
        for (Direction side : Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(side));
            if (te != null && te.getCapability(CapabilityEnergy.ENERGY, side).isPresent()) {
                tiles.put(side, te);
            }
        }
        if (tiles.size() <= 0) {
            return;
        }
        int energyPerSide = energy / tiles.size();
        int extraEnergy = 0;
        for (Map.Entry<Direction, TileEntity> entry : tiles.entrySet()) {
            Direction side = entry.getKey();
            TileEntity te = entry.getValue();
            int energyGiven = te.getCapability(CapabilityEnergy.ENERGY, side).orElse(energyHandler)
                    .receiveEnergy(energyPerSide, false);
            if (energyGiven < energyPerSide) {
                extraEnergy += energyPerSide - energyGiven;
            }
        }
        energyHandler.extractEnergy(energy - extraEnergy, false);
        markDirty();
    }
    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return energyOptional.cast();
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return interfaceOptional.cast();
        }
        return super.getCapability(capability, facing);
    }
    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("energy")) {
            CompoundNBT energyCompound = nbt.getCompound("energy");
            capacity = energyCompound.getInt("capacity");
            energyOutputRate = energyCompound.getInt("output_rate");
            energyHandler = new EnergyHandler(capacity, 0, energyOutputRate, false, true);
            reactorInterface = new ReactorEnergyInterface(energyHandler);
            energyOptional = LazyOptional.of(() -> energyHandler);
            interfaceOptional = LazyOptional.of(() -> reactorInterface);
            if (nbt.contains("handler")) {
                energyHandler.readNBT(nbt.getCompound("handler"));
            }
        }
    }
    @Override
    public CompoundNBT write (CompoundNBT nbt) {
        super.write(nbt);
        CompoundNBT energyCompound = new CompoundNBT();
        energyCompound.putInt("capacity", capacity);
        energyCompound.putInt("output_rate", energyOutputRate);
        energyCompound.put("handler", energyHandler.writeNBT());
        nbt.put("energy", energyCompound);
        return nbt;
    }
}
