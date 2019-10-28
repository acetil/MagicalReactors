package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import acetil.magicalreactors.common.capabilities.reactoritems.IReactorItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ProviderReactorItem implements ICapabilitySerializable<NBTTagCompound> {
    IReactorItem item;
    public ProviderReactorItem (IReactorItem item) {
        this.item = item;
    }
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityReactorItem.REACTOR_ITEM;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        //System.out.println("Getting item capability");
        if (capability == CapabilityReactorItem.REACTOR_ITEM) {
            return CapabilityReactorItem.REACTOR_ITEM.cast(item);
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound)CapabilityReactorItem.REACTOR_ITEM.getStorage()
                .writeNBT(CapabilityReactorItem.REACTOR_ITEM, item, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CapabilityReactorItem.REACTOR_ITEM.getStorage().readNBT(CapabilityReactorItem.REACTOR_ITEM, item, null, nbt);
    }
}
