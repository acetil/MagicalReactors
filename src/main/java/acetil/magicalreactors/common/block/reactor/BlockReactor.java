package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import acetil.magicalreactors.common.core.NuclearCreativeTab;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.tiles.TileReactorNew;

import javax.annotation.Nullable;


public class BlockReactor extends Block {
    public BlockReactor() {
        super(Properties.create(Material.ROCK));
        setRegistryName("reactor_block");
        //setHardness(4F);
        //setCreativeTab(NuclearCreativeTab.INSTANCE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactorNew();
    }

}
