package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.containers.GuiContainer;
import acetil.magicalreactors.common.containers.json.MachineContainerManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.common.constants.Constants;

import javax.annotation.Nullable;

public class BlockMachine extends Block {
    protected String machineName;
    public BlockMachine (String machineName, Properties properties) {
        super(properties);
        this.machineName = machineName;
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        System.out.println("Created tile entity!");
        return new TileMachineBase(machineName);
    }
    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                     BlockRayTraceResult rayTrace) {
        System.out.println("Machine block activated!");
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileMachineBase)) {
            System.out.println("Tile entity not instance of tileMachineBase!");
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
            public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity playerEntity) {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .orElse(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getDefaultInstance());
                return new GuiContainer(MachineContainerManager.getContainerJson(machineName),
                        Constants.MODID + ":" + machineName, windowId, inv, itemHandler, pos);
            }
        }, pos);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean isOn = worldIn.isBlockPowered(pos);
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(isOn);
            }
        }
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }
    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(worldIn.isBlockPowered(pos));
            }
        }
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            IItemHandler itemHandler = worldIn.getTileEntity(pos)
                    .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .orElse(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getDefaultInstance());
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    spawnAsEntity(worldIn, pos, itemHandler.getStackInSlot(i));
                }
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
    public String getMachineName () {
        return machineName;
    }
}
