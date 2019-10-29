package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactor;
import acetil.magicalreactors.common.capabilities.reactor.IReactorHandlerNew;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorHandlerNew;
import net.minecraftforge.registries.ForgeRegistries;

public class TileReactorNew extends TileEntity implements ITickableTileEntity {
    private static final int NUM_ITEM_SLOTS = 6;
    private ItemStackHandler itemHandler;
    private ReactorHandlerNew reactorHandler;
    private LazyOptional<IReactorHandlerNew> reactorOptional;
    public TileReactorNew () {
        super(ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(LibMisc.MODID, "reactor_tile_entity")));
        initHandlers();
    }
    private void initHandlers () {
        itemHandler = new ItemStackHandler(NUM_ITEM_SLOTS) {
          @Override
          protected void onContentsChanged (int slot) {
              TileReactorNew.this.markDirty();
          }
        };
        reactorHandler = new ReactorHandlerNew();
        reactorOptional = LazyOptional.of(() -> reactorHandler);
    }

    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction side) {
        if (capability == CapabilityReactor.CAPABILITY_REACTOR) {
            return reactorOptional.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void tick() {

    }
}
