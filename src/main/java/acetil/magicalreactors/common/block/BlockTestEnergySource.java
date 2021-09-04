package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.tiles.TileTestEnergySource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class BlockTestEnergySource extends Block implements EntityBlock {

    public BlockTestEnergySource() {
        super(Properties.of(Material.STONE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pPos, BlockState pState) {
        return new TileTestEnergySource(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return (lvl, pos, state, tile) -> {
            if (tile instanceof TileTestEnergySource) {
                ((TileTestEnergySource) tile).tickServer();
            }
        };
    }

    /*@Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Override
    public TileEntity createTileEntity (BlockState state, IBlockReader world) {
        return new TileTestEnergySource();
    }*/

}
