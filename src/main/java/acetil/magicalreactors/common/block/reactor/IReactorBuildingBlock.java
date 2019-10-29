package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IReactorBuildingBlock {
    void updateMultiblockState (BlockState state, World worldIn, BlockPos pos, boolean isMultiblock);
}
