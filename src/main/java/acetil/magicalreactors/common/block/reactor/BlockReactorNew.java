package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.tiles.TileReactorNew;

import javax.annotation.Nullable;

public class BlockReactorNew extends Block implements ITileEntityProvider {
    public BlockReactorNew() {
        super(Material.ROCK);
        setUnlocalizedName(LibMisc.MODID + ".reactor_block_new");
        setRegistryName("reactor_block_new");
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
        return new TileReactorNew();
    }

}
