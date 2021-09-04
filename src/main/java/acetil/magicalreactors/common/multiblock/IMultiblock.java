package acetil.magicalreactors.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

/*
    Abstract representation of a multiblock structure
 */
public interface IMultiblock {
    IMultiblockValidator getMultiblockValidator (LevelReader worldIn, BlockPos pos);
    boolean allowsFilledAirBlocks ();
    String getType ();
}
