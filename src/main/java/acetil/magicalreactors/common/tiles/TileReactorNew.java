package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactorNew;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorHandlerNew;

public class TileReactorNew extends TileEntity implements ITickable {
    private static final int NUM_ITEM_SLOTS = 6;
    private ItemStackHandler itemHandler;
    private ReactorHandlerNew reactorHandler;
    public TileReactorNew () {
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
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing side) {
        if (capability == CapabilityReactorNew.CAPABILITY_REACTOR) {
            return true;
        }
        return super.hasCapability(capability, side);
    }
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing side) {
        if (capability == CapabilityReactorNew.CAPABILITY_REACTOR) {
            return CapabilityReactorNew.CAPABILITY_REACTOR.cast(reactorHandler);
        }
        return super.getCapability(capability, side);
    }
    @Override
    public void update() {

    }
}
