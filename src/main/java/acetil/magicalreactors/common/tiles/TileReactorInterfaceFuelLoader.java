package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorFuelInputInterface;

public class TileReactorInterfaceFuelLoader extends BlockEntity {
    private ItemStackHandler itemHandler;
    private ReactorFuelInputInterface reactorInterface;
    private LazyOptional<IItemHandler> itemOptional;
    private LazyOptional<IReactorInterfaceHandler> interfaceOptional;
    public TileReactorInterfaceFuelLoader(BlockPos pos, BlockState state) {
        super(ModBlocks.FUEL_INTERFACE_TILE.get(), pos, state);
        itemHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged (int slot) {
                TileReactorInterfaceFuelLoader.this.setChanged();
            }
        };
        reactorInterface = new ReactorFuelInputInterface(itemHandler);
        itemOptional = LazyOptional.of(() -> itemHandler);
        interfaceOptional = LazyOptional.of(() -> reactorInterface);
    }

    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemOptional.cast();// TODO: update
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return interfaceOptional.cast();
        }
        return super.getCapability(capability, facing);
    }
    /*@Override
    public CompoundNBT write (CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("items", itemHandler.serializeNBT());
        return nbt;
    }
    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }*/

    @Override
    public CompoundTag serializeNBT () {
        var nbt = super.serializeNBT();
        nbt.put("items", itemHandler.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }
}
