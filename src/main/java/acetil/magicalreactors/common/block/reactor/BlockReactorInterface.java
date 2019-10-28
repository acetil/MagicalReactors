package acetil.magicalreactors.common.block.reactor;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BlockReactorInterface extends BlockRuneBase implements ITileEntityProvider {
    private Supplier<TileEntity> tileEntitySupplier;
    public BlockReactorInterface(String registryName, Supplier<TileEntity> teSupplier) {
        super(registryName);
        tileEntitySupplier = teSupplier;
    }
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return tileEntitySupplier.get();
    }
}
