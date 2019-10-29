package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

public class BlockRuneBase extends Block implements IReactorBuildingBlock {
    //private static final PropertyBool MULTIBLOCK_STATE = PropertyBool.create("completed_multiblock");
    public BlockRuneBase(String registryName) {
        super(Properties.create(Material.ROCK));
        setRegistryName(registryName);
        //setDefaultState(this.getBlockState().getBaseState().withProperty(MULTIBLOCK_STATE, false));
    }
    /*@Override
    public BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, MULTIBLOCK_STATE);
    }*/
    @Override
    public void updateMultiblockState(BlockState state, World worldIn, BlockPos pos, boolean isMultiblock) {
        /*if (isMultiblock && !state.getValue(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.withProperty(MULTIBLOCK_STATE, true));
        } else if (!isMultiblock && state.getValue(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.withProperty(MULTIBLOCK_STATE, false));
        }*/
    }
    public enum EnumMultiblockState implements IStringSerializable {
        NOT_IN_MULTIBLOCK("not_in_multiblock"), IN_MULTIBLOCK("in_multiblock");
        private String name;
        EnumMultiblockState (String name) {
            this.name = name;
        }
        @Override
        public String getName () {
            return name;
        }
        public int getBits () {
            if (this.name.equals("in_multiblock")) {
                return 1;
            } else {
                return 0;
            }
        }
        public static EnumMultiblockState getStateFromBits (int bits) {
            if ((bits | 1) == 1) {
                return IN_MULTIBLOCK;
            } else {
                return NOT_IN_MULTIBLOCK;
            }
        }
    }
}
