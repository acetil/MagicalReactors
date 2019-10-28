package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactorInterface;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorFuelInputInterface;

public class TileReactorInterfaceFuelLoader extends TileEntity {
    private ItemStackHandler itemHandler;
    private ReactorFuelInputInterface reactorInterface;
    public TileReactorInterfaceFuelLoader() {
        itemHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged (int slot) {
                TileReactorInterfaceFuelLoader.this.markDirty();
            }
        };
        reactorInterface = new ReactorFuelInputInterface(itemHandler);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler); // TODO: update
        } else if (capability == CapabilityReactorInterface.REACTOR_INTERFACE) {
            return CapabilityReactorInterface.REACTOR_INTERFACE.cast(reactorInterface);
        }
        return super.getCapability(capability, facing);
    }
    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("items", itemHandler.serializeNBT());
        return nbt;
    }
    @Override
    public void readFromNBT (NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("items")) {
            itemHandler.deserializeNBT(nbt.getCompoundTag("items"));
        }
    }
}
