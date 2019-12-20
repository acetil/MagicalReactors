package acetil.magicalreactors.common.block.reactor;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.tiles.TileReactorInterfaceRedstone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BlockReactorInterfaceRedstone extends BlockReactorInterface {
    public BlockReactorInterfaceRedstone(Supplier<TileEntity> teSupplier) {
        super(teSupplier);
    }


    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged (BlockState state, World worldIn, BlockPos pos, Block blockIn,
                                    BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        TileEntity te = worldIn.getTileEntity(pos);
        MagicalReactors.LOGGER.debug("Neighbour changed!");
        if (te instanceof TileReactorInterfaceRedstone) {
            ((TileReactorInterfaceRedstone) te).setPowered(worldIn.isBlockPowered(pos));
            MagicalReactors.LOGGER.debug("Set powered state!");
        }
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded (BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        TileEntity te = worldIn.getTileEntity(pos);
        if (oldState.getBlock() != state.getBlock() &&  te instanceof TileReactorInterfaceRedstone) {
            ((TileReactorInterfaceRedstone) te).setPowered(worldIn.isBlockPowered(pos));
        }
    }
}
