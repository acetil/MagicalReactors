package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class TileTestEnergySource extends TileEntity implements ITickableTileEntity {
    private LazyOptional<IEnergyStorage> energyOptional;
    private EnergyHandler energyHandler;
    public TileTestEnergySource () {
        super(ModBlocks.TEST_ENERGY_SOURCE_TILE.get());
        energyHandler = new EnergyHandler(() -> 10000000, () -> 0, () -> 1000000, false, true);
        energyOptional = LazyOptional.of(() -> energyHandler);

    }

    @Override
    public void tick() {
        if (!world.isRemote) {
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
            int energyPerSide = 1000000;
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
        }
    }
    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction side) {
        if (capability == CapabilityEnergy.ENERGY) {
            return energyOptional.cast();
        }
        return super.getCapability(capability, side);
    }
}
