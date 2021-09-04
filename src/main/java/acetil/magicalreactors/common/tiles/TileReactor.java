package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactor;
import acetil.magicalreactors.common.capabilities.reactor.IReactorHandlerNew;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorHandlerNew;

public class TileReactor extends BlockEntity{
    private static final int NUM_ITEM_SLOTS = 6;
    private ItemStackHandler itemHandler;
    private ReactorHandlerNew reactorHandler;
    private LazyOptional<IReactorHandlerNew> reactorOptional;
    public TileReactor(BlockPos pos, BlockState state) {
        super(ModBlocks.REACTOR_TILE_ENTITY.get(), pos, state);
        initHandlers();
    }
    private void initHandlers () {
        itemHandler = new ItemStackHandler(NUM_ITEM_SLOTS) {
          @Override
          protected void onContentsChanged (int slot) {
              TileReactor.this.setChanged();
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

    public void tickServer() {
        this.setChanged();
    }

    /*@Override
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
    }*/

    @Override
    public CompoundTag serializeNBT () {
        var nbt = super.serializeNBT();
        nbt.put("reactor", reactorHandler.writeNBT());
        nbt.put("items", itemHandler.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("reactor")) {
            reactorHandler.readNBT(nbt.getCompound("reactor"));
        }
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }
}
