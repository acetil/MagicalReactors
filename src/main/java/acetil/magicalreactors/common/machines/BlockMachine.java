package acetil.magicalreactors.common.machines;

import acetil.magicalreactors.common.containers.GuiContainer;
import acetil.magicalreactors.common.containers.json.MachineContainerManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    /*@Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        System.out.println("Created tile entity!");
        return new TileMachineBase(machineName);
    }*/
   /* @Override
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
    }*/

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use (BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var result =  super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (pLevel.isClientSide()) {
            return result;
        }
        var entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof TileMachineBase) {

        }
    }

    @SuppressWarnings("deprecation")
    /*@Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean isOn = worldIn.isBlockPowered(pos);
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(isOn);
            }
        }
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }*/
    /*@Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof TileMachineBase) {
                ((TileMachineBase)worldIn.getTileEntity(pos)).setPoweredState(worldIn.isBlockPowered(pos));
            }
        }
    }*/
    /*@Override
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
    }*/
    public String getMachineName () {
        return machineName;
    }
}
