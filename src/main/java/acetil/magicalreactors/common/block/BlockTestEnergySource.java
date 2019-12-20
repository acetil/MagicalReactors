package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.tiles.TileTestEnergySource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockTestEnergySource extends Block {

    public BlockTestEnergySource() {
        super(Properties.create(Material.ROCK));
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
    @Override
    public TileEntity createTileEntity (BlockState state, IBlockReader world) {
        return new TileTestEnergySource();
    }
}
