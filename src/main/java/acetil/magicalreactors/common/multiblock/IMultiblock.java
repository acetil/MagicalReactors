package acetil.magicalreactors.common.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
    Abstract representation of a multiblock structure
 */
public interface IMultiblock {
    IMultiblockValidator getMultiblockValidator (World worldIn, BlockPos pos);
    boolean allowsFilledAirBlocks ();
    String getType ();
}
