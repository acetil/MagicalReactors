package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

public class BlockRuneBase extends Block implements IReactorBuildingBlock {
    private static final BooleanProperty MULTIBLOCK_STATE = BooleanProperty.create("completed_multiblock");
    public BlockRuneBase(String registryName) {
        super(Properties.create(Material.ROCK));
        setRegistryName(registryName);
        setDefaultState(getStateContainer().getBaseState().with(MULTIBLOCK_STATE, false));
    }

    @Override
    public void updateMultiblockState(BlockState state, World worldIn, BlockPos pos, boolean isMultiblock) {
        if (isMultiblock && !state.get(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.with(MULTIBLOCK_STATE, true));
        } else if (!isMultiblock && state.get(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.with(MULTIBLOCK_STATE, false));
        }
    }
    @Override
    protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
        builder.add(MULTIBLOCK_STATE);
    }

}
