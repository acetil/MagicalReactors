package acetil.magicalreactors.common.block.reactor;


import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.NuclearMod;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.tiles.TileReactorController;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;

public class BlockReactorController extends Block implements ITileEntityProvider {

    public BlockReactorController () {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + ".reactor_controller");
        setRegistryName("reactor_controller");
        setHardness(4F);
        setCreativeTab(NuclearCreativeTab.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    public void initModel () {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileReactorController();
    }
    @Override
    public void onBlockPlacedBy (World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // TODO: add other details
        TileEntity te =  worldIn.getTileEntity(pos);
        if (te instanceof TileReactorController) {
            //((TileReactorController)te).setPosition(worldIn, pos);
        } else {
            NuclearMod.logger.log(Level.WARN,
                    String.format("Null reactor controller TE added at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
        }
    }
    @Override
    public boolean onBlockActivated (World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileReactorController) {
            NuclearMod.logger.log(Level.INFO, "Checking multiblock");
            ((TileReactorController) te).updateMultiblock();
            return true;
        }
        return false;
    }
}
