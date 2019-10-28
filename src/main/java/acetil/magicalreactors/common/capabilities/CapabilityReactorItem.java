package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactoritems.IReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.ReactorItem;

public class CapabilityReactorItem {
    @CapabilityInject(IReactorItem.class)
    public static Capability<IReactorItem> REACTOR_ITEM = null;

    public static void register () {
        CapabilityManager.INSTANCE.register(IReactorItem.class, new Capability.IStorage<IReactorItem>() {
            public NBTBase writeNBT (Capability<IReactorItem> capability, IReactorItem instance, EnumFacing side) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("heat", instance.getHeat());
                nbt.setBoolean("Unbreakable", false);
                return nbt;
            }
            public void readNBT (Capability<IReactorItem> capability, IReactorItem instance, EnumFacing side, NBTBase nbt) {
                instance.setHeat(((NBTTagCompound)nbt).getInteger("heat"));
            }
        }, ReactorItem::new);
    }
}
