package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMachine extends Block {
    protected String machineName;
    public BlockMachine (String name, String machineName) {
        super(Properties.create(Material.ROCK));
        setRegistryName(name);
        this.machineName = machineName;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMachineBase(machineName);
    }
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                     BlockRayTraceResult rayTrace) {

        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileMachineBase)) {
            return false;
        }
        ITextComponent localisedName = this.getNameTextComponent();
        NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return localisedName;
            }

            @Nullable
            @Override
            public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                ItemStackHandler itemHandler = ((TileMachineBase) te).itemHandler;

                return null;
            }
        }, pos);
        return true;
    }
    /*public void onBlockAdded (World worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.isRemote) {
            boolean isOn = worldIn.isBlockPowered(pos);
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(isOn);
            }
        }
        super.onBlockAdded(worldIn, pos, state);
    } */
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(worldIn.isBlockPowered(pos));
            }
        }
    }

    /*@Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos,@Nonnull BlockState state) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
            IItemHandler itemStackHandler = worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).;
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                    spawnAsEntity(worldIn, pos, itemStackHandler.getStackInSlot(i));
                }
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }*/
}
