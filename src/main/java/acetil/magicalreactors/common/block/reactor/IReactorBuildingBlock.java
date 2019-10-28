package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IReactorBuildingBlock {
    void updateMultiblockState (IBlockState state, World worldIn, BlockPos pos, boolean isMultiblock);
}
