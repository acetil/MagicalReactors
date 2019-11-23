package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import acetil.magicalreactors.common.tiles.TileReactor;

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
        return new TileReactor();
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }

}
