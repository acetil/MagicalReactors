package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BlockReactorInterface extends BlockRuneBase {
    private Supplier<TileEntity> tileEntitySupplier;
    public BlockReactorInterface(Supplier<TileEntity> teSupplier, Properties properties) {
        super(properties);
        tileEntitySupplier = teSupplier;
    }
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileEntitySupplier.get();
    }
    @Override
    public boolean hasTileEntity (BlockState state) {
        return true;
    }
}
