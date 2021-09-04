package acetil.magicalreactors.common.block.reactor;

import net.minecraft.core.BlockPos;
import acetil.magicalreactors.common.tiles.TileReactor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;


public class BlockReactor extends Block implements EntityBlock {
    public BlockReactor(Properties properties) {
        super(properties);
        //setHardness(4F);
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pPos, BlockState pState) {
        return new TileReactor(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, state, tile) -> {
            if (tile instanceof TileReactor) {
                ((TileReactor) tile).tickServer();
            }
        };
    }


    /*

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactor();
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
*/


}
