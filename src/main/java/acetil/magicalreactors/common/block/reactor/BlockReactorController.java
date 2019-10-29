package acetil.magicalreactors.common.block.reactor;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.tiles.TileReactorController;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public class BlockReactorController extends Block{

    public BlockReactorController () {
        super(Properties.create(Material.ROCK));
        setRegistryName("reactor_controller");
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactorController();
    }
    @Override
    public void onBlockPlacedBy (World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        // TODO: add other details
        TileEntity te =  worldIn.getTileEntity(pos);
        if (te instanceof TileReactorController) {
            //((TileReactorController)te).setPosition(worldIn, pos);
        } else {
            MagicalReactors.LOGGER.log(Level.WARN,
                    String.format("Null reactor controller TE added at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
        }
    }
    // TODO: figure out deprecation
    @Override
    public boolean onBlockActivated (BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
                                     Hand hand, BlockRayTraceResult hit) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileReactorController) {
            MagicalReactors.LOGGER.log(Level.INFO, "Checking multiblock");
            ((TileReactorController) te).updateMultiblock();
            return true;
        }
        return false;
    }
}
