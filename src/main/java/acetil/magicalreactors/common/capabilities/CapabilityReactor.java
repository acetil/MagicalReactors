package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.lib.LibReactor;

public class CapabilityReactor {
    @CapabilityInject(IReactorHandler.class)
    public static Capability<IReactorHandler> REACTOR_HANDLER = null;
    public static void register () {
        CapabilityManager.INSTANCE.register(IReactorHandler.class, new Capability.IStorage<IReactorHandler>() {
            public NBTBase writeNBT (Capability<IReactorHandler> capability, IReactorHandler instance, EnumFacing side) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("ticks", instance.getTicksSinceUpdate());
                nbt.setBoolean("isActive", instance.isActive());
                nbt.setInteger("heat", instance.getHullHeat());
                return nbt;
            }
            public void readNBT (Capability<IReactorHandler> capability, IReactorHandler instance, EnumFacing side, NBTBase nbt) {
                NBTTagCompound tags = (NBTTagCompound) nbt;
                instance.setActive(tags.getBoolean("isActive"));
                instance.setTicksSinceUpdate(tags.getInteger("ticks"));
                instance.setHullHeat(tags.getInteger("heat"));
            }
        }, () -> new ReactorHandler(LibReactor.DEFAULT_MAX_HEAT, 6, 9));
    }
}
