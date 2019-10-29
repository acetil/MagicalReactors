package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDistiller extends Block {
    private static final EnumProperty<EnumDistillState> BLOCK_POSITION = EnumProperty.create("block_pos", EnumDistillState.class);
    private String machineName;
    private int bottomSlots;
    public BlockDistiller(String name, String machineName, int bottomSlots) {
        super(Properties.create(Material.ROCK));
        setRegistryName(name);
        //setDefaultState(this.getBlockState().getBaseState().withProperty(blockPosition, EnumDistillState.NONE));
        this.machineName = machineName;
        this.bottomSlots = bottomSlots;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement (BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
                                           BlockPos currentPos, BlockPos facingPos) {
        BlockState newState = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        if (!worldIn.isRemote()) {
            if (facingState.getBlock() == this && stateIn.get(BLOCK_POSITION).isSameState(EnumDistillState.NONE)) {
                // both are none
                if (facing == Direction.DOWN && !facingState.get(BLOCK_POSITION).isSameState(EnumDistillState.TOP)) {
                    newState = newState.with(BLOCK_POSITION, EnumDistillState.TOP);
                } else if (facing == Direction.UP && !facingState.get(BLOCK_POSITION).isSameState(EnumDistillState.TOP)) {
                    newState = newState.with(BLOCK_POSITION, EnumDistillState.BOTTOM);
                }
            } else if (facingState.getBlock() != this && !stateIn.get(BLOCK_POSITION).isSameState(EnumDistillState.NONE)) {
                if ((facing == Direction.DOWN && stateIn.get(BLOCK_POSITION).isSameState(EnumDistillState.TOP)) ||
                        (facing == Direction.UP && stateIn.get(BLOCK_POSITION).isSameState(EnumDistillState.BOTTOM))) {
                    // previous block has been removed
                    newState = newState.with(BLOCK_POSITION, EnumDistillState.NONE);
                }
            }
            if (!newState.get(BLOCK_POSITION).isSameState(stateIn.get(BLOCK_POSITION))) {
                if (worldIn.getTileEntity(currentPos) instanceof TileMachineDistiller) {
                    TileMachineDistiller te = (TileMachineDistiller) worldIn.getTileEntity(currentPos);
                    te.updateDistillState(newState.get(BLOCK_POSITION));
                    if (newState.get(BLOCK_POSITION).isSameState(EnumDistillState.TOP)
                            && worldIn.getTileEntity(facingPos) instanceof TileMachineDistiller) {
                        te.setController((TileMachineDistiller) worldIn.getTileEntity(facingPos));
                    } else {
                        te.setController(null);
                    }
                }
            }
        }
        return newState;
    }

    @Override
    protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BLOCK_POSITION);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMachineDistiller(machineName, bottomSlots);
    }
} enum EnumDistillState implements IStringSerializable {
    NONE("none"), BOTTOM("bottom"), TOP("top");
    private String name;
    EnumDistillState(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }
    public boolean isSameState (EnumDistillState state) {
        return state.getName().equals(name);
    }
}