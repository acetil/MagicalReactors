package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import acetil.magicalreactors.common.capabilities.reactor.ReactorEnergyInterface;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class TileReactorInterfaceEnergy extends BlockEntity {
    private EnergyHandler energyHandler;
    private ReactorEnergyInterface reactorInterface;
    private LazyOptional<IEnergyStorage> energyOptional = LazyOptional.empty();
    private LazyOptional<IReactorInterfaceHandler> interfaceOptional = LazyOptional.empty();
    private int capacity;
    private int energyOutputRate;
    private BlockPos pos;
    public TileReactorInterfaceEnergy (BlockPos pos, BlockState state, int capacity, int energyOutputRate) {
        super(ModBlocks.ENERGY_INTERFACE_TILE.get(), pos, state);
        this.pos = pos;
        energyHandler = new EnergyHandler(() -> capacity, () -> capacity, () -> energyOutputRate, false, true);
        reactorInterface = new ReactorEnergyInterface(energyHandler);
        energyOptional = LazyOptional.of(() -> energyHandler);
        interfaceOptional = LazyOptional.of(() -> reactorInterface);
        this.capacity = capacity;
        this.energyOutputRate = energyOutputRate;
    }
    public TileReactorInterfaceEnergy (BlockPos pos, BlockState state) {
        this(pos, state, 10000, 1000);
    }

    public void tickServer() {
        giveEnergy(Math.min(energyOutputRate, energyHandler.getEnergyStored()));
        this.setChanged();
    }
    private void giveEnergy (int energy) {
        HashMap<Direction, BlockEntity> tiles = new HashMap<>();
        for (Direction side : Direction.values()) {
            var te = level.getBlockEntity(pos.offset(side.getNormal()));
            if (te != null && te.getCapability(CapabilityEnergy.ENERGY, side).isPresent()) {
                tiles.put(side, te);
            }
        }
        if (tiles.size() <= 0) {
            return;
        }
        int energyPerSide = energy / tiles.size();
        int extraEnergy = 0;
        for (Map.Entry<Direction, BlockEntity> entry : tiles.entrySet()) {
            Direction side = entry.getKey();
            var te = entry.getValue();
            int energyGiven = te.getCapability(CapabilityEnergy.ENERGY, side).orElse(energyHandler)
                    .receiveEnergy(energyPerSide, false);
            if (energyGiven < energyPerSide) {
                extraEnergy += energyPerSide - energyGiven;
            }
        }
        energyHandler.extractEnergy(energy - extraEnergy, false);
        setChanged();
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
    /*@Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("energy")) {
            CompoundNBT energyCompound = nbt.getCompound("energy");
            capacity = energyCompound.getInt("capacity");
            energyOutputRate = energyCompound.getInt("output_rate");
            energyHandler = new EnergyHandler(() -> capacity, () -> capacity, () -> energyOutputRate, false, true);
            reactorInterface = new ReactorEnergyInterface(energyHandler);
            energyOptional = LazyOptional.of(() -> energyHandler);
            interfaceOptional = LazyOptional.of(() -> reactorInterface);
            if (energyCompound.contains("handler")) {
                energyHandler.readNBT(energyCompound.getCompound("handler"));
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
    }*/

    @Override
    public CompoundTag serializeNBT () {
        var nbt = super.serializeNBT();

        var energyCompound = new CompoundTag();
        energyCompound.putInt("capacity", capacity);
        energyCompound.putInt("output_rate", energyOutputRate);
        energyCompound.put("handler", energyHandler.writeNBT());

        nbt.put("energy", energyCompound);
        return nbt;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);

        if (nbt.contains("energy")) {
            var energyCompound = nbt.getCompound("energy");
            capacity = energyCompound.getInt("capacity");
            energyOutputRate = energyCompound.getInt("output_rate");
            energyHandler.readNBT(energyCompound.getCompound("handler"));

    }
}
