package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorRedstoneInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileReactorInterfaceRedstone extends TileEntity {
    private ReactorRedstoneInterface redstoneInterface;
    private LazyOptional<IReactorInterfaceHandler> redstoneOptional;
    public TileReactorInterfaceRedstone() {
        super(ModBlocks.REDSTONE_INTERFACE_TILE.get());
        System.out.println("Is TileType null: " + (ModBlocks.REDSTONE_INTERFACE_TILE == null));
        redstoneInterface = new ReactorRedstoneInterface();
        redstoneOptional = LazyOptional.of(() -> redstoneInterface);
    }
    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction side) {
        if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return redstoneOptional.cast();
        }
        return super.getCapability(capability, side);
    }
    public void setPowered (boolean isPowered) {
        redstoneInterface.setPowered(isPowered);
    }
}
