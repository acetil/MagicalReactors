package acetil.magicalreactors.common.block.reactor;


import acetil.magicalreactors.common.capabilities.CapabilityReactorController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.tiles.TileReactorController;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public class BlockReactorController extends BlockRuneBase {

    public BlockReactorController (Properties properties) {
        super(properties);
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactorController();
    }
    // TODO: figure out deprecation
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
    }
}
