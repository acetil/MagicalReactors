package acetil.magicalreactors.common.block.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface IReactorBuildingBlock {
    void updateMultiblockState (BlockState state, LevelReader worldIn, BlockPos pos, boolean isMultiblock);
}
