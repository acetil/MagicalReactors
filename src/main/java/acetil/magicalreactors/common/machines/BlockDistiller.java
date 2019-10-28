package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDistiller extends Block implements ITileEntityProvider {
    private static final PropertyEnum<EnumDistillState> blockPosition = PropertyEnum.create("block_pos", EnumDistillState.class);
    private String machineName;
    private int bottomSlots;
    public BlockDistiller(String name, String machineName, int bottomSlots) {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + "." + name);
        setRegistryName(name);
        setHardness(4F);
        setCreativeTab(NuclearCreativeTab.INSTANCE);
        setDefaultState(this.getBlockState().getBaseState().withProperty(blockPosition, EnumDistillState.NONE));
        this.machineName = machineName;
        this.bottomSlots = bottomSlots;
    }
    public void initModel () {
        // TODO update json
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged (IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        // TODO tidy up
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof TileMachineDistiller) {
                // for tile entity created with default constructor case, this sets it
                ((TileMachineDistiller)worldIn.getTileEntity(pos)).setBottomSlots(bottomSlots);
            }
            if (state.getValue(blockPosition) == EnumDistillState.NONE && worldIn.getBlockState(fromPos).getBlock() == this) {
                TileMachineDistiller te = null;
                if (worldIn.getTileEntity(pos) instanceof TileMachineDistiller) {
                    te = (TileMachineDistiller) worldIn.getTileEntity(pos);
                }
                if (pos.getX() == fromPos.getX() && pos.getZ() == fromPos.getZ()) {
                    if (pos.add(0, 1, 0).equals(fromPos)) {
                        worldIn.setBlockState(pos, state.withProperty(blockPosition, EnumDistillState.BOTTOM));
                        if (te != null) {
                            te.updateDistillState(EnumDistillState.BOTTOM);
                        }
                    } else if (pos.add(0, -1, 0).equals(fromPos)) {
                        worldIn.setBlockState(pos, state.withProperty(blockPosition, EnumDistillState.TOP));
                        if (te != null) {
                            te.updateDistillState(EnumDistillState.TOP);
                            if (worldIn.getTileEntity(fromPos) instanceof TileMachineDistiller) {
                                te.setController((TileMachineDistiller)worldIn.getTileEntity(fromPos));
                            }
                        }
                    }
                }
            } else if (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() != this
                    && worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() != this && state.getValue(blockPosition) != EnumDistillState.NONE) {
                worldIn.setBlockState(pos, state.withProperty(blockPosition, EnumDistillState.NONE));
                if (worldIn.getTileEntity(pos) instanceof  TileMachineDistiller) {
                    ((TileMachineDistiller)worldIn.getTileEntity(pos)).updateDistillState(EnumDistillState.NONE);
                    ((TileMachineDistiller)worldIn.getTileEntity(pos)).setController(null);
                }
            }
        }
    }
    @Override
    public BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, blockPosition);
    }
    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta (int meta) {
        return this.getBlockState().getBaseState().withProperty(blockPosition, EnumDistillState.getStateFromBits(meta >> 2));
    }
    @Override
    public int getMetaFromState (IBlockState state) {
        int meta = 0;
        meta |= EnumDistillState.getBits(state.getValue(blockPosition)) << 2;
        return meta;
    }
    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos,@Nonnull IBlockState state) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler itemStackHandler = worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                    spawnAsEntity(worldIn, pos, itemStackHandler.getStackInSlot(i));
                }
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileMachineDistiller(machineName, bottomSlots);
    }
} enum EnumDistillState implements IStringSerializable {
    NONE("none"), BOTTOM("bottom"), TOP("top");
    private String name;
    EnumDistillState(String name) {
        this.name = name;
    }
    public static int getBits (EnumDistillState name) {
        if (name == NONE) {
            return 0; // 00
        } else if (name == BOTTOM) {
            return 1; //01
        } else {
            return 2; //10
        }
    }
    public static EnumDistillState getStateFromBits (int bits) {
        if (bits == 0) {
            return NONE;
        } else if (bits == 1) {
            return BOTTOM;
        } else {
            return TOP;
        }
    }
    @Override
    public String getName() {
        return name;
    }
}