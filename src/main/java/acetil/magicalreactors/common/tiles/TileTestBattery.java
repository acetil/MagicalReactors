package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTestBattery extends TileEntity implements ITickableTileEntity {
    private EnergyHandler energyHandler;
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.empty();
    public TileTestBattery() {
        super(ModBlocks.TEST_BATTERY_TILE);
        energyHandler = new EnergyHandler(() -> 1000000, () -> 10000, () ->1000, true, true);
        energyStorage = LazyOptional.of(() -> energyHandler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {

    }
}
