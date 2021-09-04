package acetil.magicalreactors.common.block.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;

public interface IReactorBuildingBlock {
    void updateMultiblockState (BlockState state, LevelWriter worldIn, BlockPos pos, boolean isMultiblock);
}
