package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.tiles.TileReactor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.lib.LibMisc;

public class BlockReactor extends Block implements ITileEntityProvider {
    public static final int GUI_ID = LibGui.REACTOR_ID;
    private boolean isOn = false;
    public BlockReactor () {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + ".reactor_block");
        setRegistryName("reactor_block");
        setHardness(4F);
        setCreativeTab(NuclearCreativeTab.INSTANCE);

    }

    @SideOnly(Side.CLIENT)
    public void initModel () {
        // TODO update json
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileReactor(); //TODO
    }

    private TileEntity getTilteEntity (World world, BlockPos pos) {
        return world.getTileEntity(pos); //TODO
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileReactor)) {
            return false;
        }
        //System.out.println("Opening reactor gui");
        player.openGui(NuclearMod.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true; //TODO
    }
    public void onBlockAdded (World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote){
            isOn = worldIn.isBlockPowered(pos);
            if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileReactor) {
                ((TileReactor)worldIn.getTileEntity(pos)).isOn = isOn;
            }


        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            isOn = worldIn.isBlockPowered(pos);
            if (worldIn.getTileEntity(pos) instanceof TileReactor) {
                ((TileReactor)worldIn.getTileEntity(pos)).isOn = isOn;
            }
        }
    }
    // TODO add rotation code

}
