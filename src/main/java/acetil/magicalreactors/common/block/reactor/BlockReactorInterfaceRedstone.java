package acetil.magicalreactors.common.block.reactor;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.tiles.TileReactorInterfaceRedstone;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BlockReactorInterfaceRedstone extends BlockReactorInterface {

    public BlockReactorInterfaceRedstone(BiFunction<BlockPos, BlockState, BlockEntity> teSupplier, Properties properties) {
        super(teSupplier, properties);
    }


    /*@Override
    @SuppressWarnings("deprecation")
    public void neighborChanged (BlockState state, LevelReader worldIn, BlockPos pos, Block blockIn,
                                 BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        TileEntity te = worldIn.getTileEntity(pos);
        MagicalReactors.LOGGER.debug("Neighbour changed!");
        if (te instanceof TileReactorInterfaceRedstone) {
            ((TileReactorInterfaceRedstone) te).setPowered(worldIn.isBlockPowered(pos));
            MagicalReactors.LOGGER.debug("Set powered state!");
        }
    }*/

    @SuppressWarnings("deprecation") // TODO
    @Override
    public void neighborChanged (BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        var te = pLevel.getBlockEntity(pPos);
        MagicalReactors.LOGGER.debug("Neighbour changed!");
        if (te instanceof TileReactorInterfaceRedstone) {
            var signal = pLevel.getBestNeighborSignal(pPos);
            ((TileReactorInterfaceRedstone) te).setPowered(signal > 0);
        }
    }

/*    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded (BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        TileEntity te = worldIn.getTileEntity(pos);
        if (oldState.getBlock() != state.getBlock() &&  te instanceof TileReactorInterfaceRedstone) {
            ((TileReactorInterfaceRedstone) te).setPowered(worldIn.isBlockPowered(pos));
        }
    }*/

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace (BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        var te = pLevel.getBlockEntity(pPos);
        if (!pOldState.is(pState.getBlock()) && te instanceof TileReactorInterfaceRedstone) {
            ((TileReactorInterfaceRedstone) te).setPowered(pLevel.getBestNeighborSignal(pPos) > 0);
        }
    }
}
