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
import acetil.magicalreactors.common.capabilities.reactor.ReactorByproductInterface;
import acetil.magicalreactors.common.lib.LibReactor;

public class TileReactorInterfaceByproduct extends BlockEntity {
    private ItemStackHandler itemHandler;
    private ReactorByproductInterface interfaceHandler;
    private LazyOptional<IItemHandler> itemOptional;
    private LazyOptional<IReactorInterfaceHandler> interfaceOptional;
    public TileReactorInterfaceByproduct (BlockPos pos, BlockState state) {
        super(ModBlocks.BYPRODUCT_INTERFACE_TILE.get(), pos, state);
        itemHandler = new ItemStackHandler(LibReactor.BYPRODUCT_INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged (int slot) {
                TileReactorInterfaceByproduct.this.setChanged();
            }
        };
        interfaceHandler = new ReactorByproductInterface(itemHandler);
        itemOptional = LazyOptional.of(() -> itemHandler);
        interfaceOptional = LazyOptional.of(() -> interfaceHandler);
    }
    @Override
    public <T> LazyOptional<T> getCapability (Capability<T> capability, Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemOptional.cast();
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return interfaceOptional.cast();
        }
        return super.getCapability(capability, facing);
    }
    /*@Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }
    @Override
    public CompoundNBT write (CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("items", itemHandler.serializeNBT());
        return nbt;
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
