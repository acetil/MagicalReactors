package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactor;
import acetil.magicalreactors.common.capabilities.reactor.IReactorHandlerNew;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorHandlerNew;

public class TileReactor extends TileEntity implements ITickableTileEntity {
    private static final int NUM_ITEM_SLOTS = 6;
    private ItemStackHandler itemHandler;
    private ReactorHandlerNew reactorHandler;
    private LazyOptional<IReactorHandlerNew> reactorOptional;
    public TileReactor() {
        super(ModBlocks.REACTOR_TILE_ENTITY);
        initHandlers();
    }
    private void initHandlers () {
        itemHandler = new ItemStackHandler(NUM_ITEM_SLOTS) {
          @Override
          protected void onContentsChanged (int slot) {
              TileReactor.this.markDirty();
          }
        };
        reactorHandler = new ReactorHandlerNew();
        reactorHandler.setItemHandler(itemHandler);
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
        this.markDirty();
    }

    @Override
    public void read (CompoundNBT compound) {
        super.read(compound);
        CompoundNBT reactorCompound = compound.getCompound("reactor");
        reactorHandler.readNBT(reactorCompound);
        itemHandler.deserializeNBT(compound.getCompound("items"));
        System.out.println("Reading nbt!");
    }

    @Override
    public CompoundNBT write (CompoundNBT compound) {
        super.write(compound);
        compound.put("reactor", reactorHandler.writeNBT());
        compound.put("items", itemHandler.serializeNBT());
        System.out.println("Writing nbt!");
        return compound;
    }
}
