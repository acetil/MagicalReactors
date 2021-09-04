package acetil.magicalreactors.common.block.reactor;

import acetil.magicalreactors.common.tiles.TileReactorInterfaceEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BlockReactorInterface extends BlockRuneBase implements EntityBlock {
    private BiFunction<BlockPos, BlockState, BlockEntity> tileEntitySupplier;
    public BlockReactorInterface(BiFunction<BlockPos, BlockState, BlockEntity> teSupplier, Properties properties) {
        super(properties);
        tileEntitySupplier = teSupplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pPos, BlockState pState) {
        return tileEntitySupplier.apply(pPos, pState);
    }

    // TODO TODO TODO
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (lvl, pos, state, tile) -> {
            if (tile instanceof TileReactorInterfaceEnergy) {
                ((TileReactorInterfaceEnergy) tile).tickServer();
            }
        };
    }
    /*@Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileEntitySupplier.get();
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }*/

}
