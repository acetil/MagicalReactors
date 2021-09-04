package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class TileTestEnergySource extends BlockEntity {
    private LazyOptional<IEnergyStorage> energyOptional;
    private EnergyHandler energyHandler;
    private BlockPos pos;
    public TileTestEnergySource (BlockPos pos, BlockState state) {
        super(ModBlocks.TEST_ENERGY_SOURCE_TILE.get(), pos, state);
        energyHandler = new EnergyHandler(() -> 10000000, () -> 0, () -> 1000000, false, true);
        energyOptional = LazyOptional.of(() -> energyHandler);

        this.pos = pos;
    }

    public void tickServer () {
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
        int energyPerSide = 1000000;
        int extraEnergy = 0;
        for (Map.Entry<Direction, BlockEntity> entry : tiles.entrySet()) {
            Direction side = entry.getKey();
            BlockEntity te = entry.getValue();
            int energyGiven = te.getCapability(CapabilityEnergy.ENERGY, side).orElse(energyHandler)
                    .receiveEnergy(energyPerSide, false);
            if (energyGiven < energyPerSide) {
                extraEnergy += energyPerSide - energyGiven;
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
