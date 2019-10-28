package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

public class BlockRuneBase extends Block implements IReactorBuildingBlock {
    private static final PropertyBool MULTIBLOCK_STATE = PropertyBool.create("completed_multiblock");
    public BlockRuneBase(String registryName) {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
        setRegistryName(registryName);
        setHardness(4F);
        setCreativeTab(NuclearCreativeTab.INSTANCE);
        setDefaultState(this.getBlockState().getBaseState().withProperty(MULTIBLOCK_STATE, false));
    }
    public void initModel () {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    @Override
    public BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, MULTIBLOCK_STATE);
    }
    @Override
    public void updateMultiblockState(IBlockState state, World worldIn, BlockPos pos, boolean isMultiblock) {
        if (isMultiblock && !state.getValue(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.withProperty(MULTIBLOCK_STATE, true));
        } else if (!isMultiblock && state.getValue(MULTIBLOCK_STATE)) {
            worldIn.setBlockState(pos, state.withProperty(MULTIBLOCK_STATE, false));
        }
    }
    @Override
    public int getMetaFromState (IBlockState state) {
        int meta = 0;
        if (state.getValue(MULTIBLOCK_STATE)) {
            meta |= 1;
        }
        return meta;
    }
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta (int meta) {
        return this.getDefaultState().withProperty(MULTIBLOCK_STATE, (meta & 1) == 1);
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
