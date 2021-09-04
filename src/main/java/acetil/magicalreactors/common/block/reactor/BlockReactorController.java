package acetil.magicalreactors.common.block.reactor;


import acetil.magicalreactors.common.capabilities.CapabilityReactorController;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.tiles.TileReactorController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class BlockReactorController extends BlockRuneBase implements EntityBlock {

    public BlockReactorController (Properties properties) {
        super(properties);
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
    }
   /* @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactorController();
    }*/
   /* // TODO: figure out deprecation
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated (BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                     Hand hand, BlockRayTraceResult hit) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileReactorController) {
            MagicalReactors.LOGGER.log(Level.INFO, "Checking multiblock");
            ((TileReactorController) te).updateMultiblock();
            if (te.getCapability(CapabilityReactorController.REACTOR_CONTROLLER).isPresent()) {
                te.getCapability(CapabilityReactorController.REACTOR_CONTROLLER)
                        .orElse(CapabilityReactorController.REACTOR_CONTROLLER.getDefaultInstance()).debugPrint(playerIn);
            }
            return true;
        }
        return false;
    }*/

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use (BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var te = pLevel.getBlockEntity(pPos);
        if (te instanceof TileReactorController) {
            MagicalReactors.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Checking multiblock");
            ((TileReactorController) te).updateMultiblock();
            if (te.getCapability(CapabilityReactorController.REACTOR_CONTROLLER).isPresent()) {
                te.getCapability(CapabilityReactorController.REACTOR_CONTROLLER)
                        .orElseThrow(() -> new RuntimeException("Bad optional!")).debugPrint(pPlayer); // TODO
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pPos, BlockState pState) {
        return new TileReactorController(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (lvl, pos, state, tile) -> {
            if (tile instanceof TileReactorController) {
                ((TileReactorController) tile).tickServer();
            }
        };
    }
}
