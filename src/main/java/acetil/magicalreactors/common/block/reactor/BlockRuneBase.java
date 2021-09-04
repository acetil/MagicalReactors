package acetil.magicalreactors.common.block.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockRuneBase extends Block implements IReactorBuildingBlock {
    public static final BooleanProperty MULTIBLOCK_STATE = BooleanProperty.create("completed_multiblock");
    public BlockRuneBase(Properties properties) {
        super(properties);
        //setDefaultState(getStateContainer().getBaseState().with(MULTIBLOCK_STATE, false));
        registerDefaultState(getStateDefinition().any().setValue(MULTIBLOCK_STATE, false));
    }

    @Override
    public void updateMultiblockState(BlockState state, LevelWriter worldIn, BlockPos pos, boolean isMultiblock) {
        if (isMultiblock && !state.getValue(MULTIBLOCK_STATE)) {
            //worldIn.setBlockState(pos, state.with(MULTIBLOCK_STATE, true));
            worldIn.setBlock(pos, state.setValue(MULTIBLOCK_STATE, true), 1 | 2);
            //worldIn
        } else if (!isMultiblock && state.getValue(MULTIBLOCK_STATE)) {
            //worldIn.setBlockState(pos, state.with(MULTIBLOCK_STATE, false));
            worldIn.setBlock(pos, state.setValue(MULTIBLOCK_STATE, false), 1 | 2);
        }
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(MULTIBLOCK_STATE);
    }
}
