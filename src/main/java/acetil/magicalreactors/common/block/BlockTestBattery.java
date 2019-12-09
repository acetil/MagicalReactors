package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.tiles.TileTestBattery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockTestBattery extends Block {
    public BlockTestBattery () {
        super(Properties.create(Material.ROCK));
        setRegistryName(new ResourceLocation(LibMisc.MODID, "test_battery"));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileTestBattery();
    }
}
