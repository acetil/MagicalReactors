package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorInterfaceHandler;

import javax.annotation.Nullable;

public class CapabilityReactorInterface {
    @CapabilityInject(IReactorInterfaceHandler.class)
    public static Capability<IReactorInterfaceHandler> REACTOR_INTERFACE;
    public static void register () {
        CapabilityManager.INSTANCE.register(IReactorInterfaceHandler.class, new Capability.IStorage<IReactorInterfaceHandler>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IReactorInterfaceHandler> capability, IReactorInterfaceHandler instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IReactorInterfaceHandler> capability, IReactorInterfaceHandler instance, EnumFacing side, NBTBase nbt) {

            }
        }, ReactorInterfaceHandler::new);
    }
}
