package acetil.magicalreactors.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactor.IReactorInterfaceHandler;
import acetil.magicalreactors.common.capabilities.reactor.ReactorInterfaceHandler;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public class CapabilityReactorInterface {
    @CapabilityInject(IReactorInterfaceHandler.class)
    public static Capability<IReactorInterfaceHandler> REACTOR_INTERFACE;
    public static void register (RegisterCapabilitiesEvent event) {
        event.register(IReactorInterfaceHandler.class);
        /*CapabilityManager.INSTANCE.register(IReactorInterfaceHandler.class, new Capability.IStorage<IReactorInterfaceHandler>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IReactorInterfaceHandler> capability, IReactorInterfaceHandler instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IReactorInterfaceHandler> capability, IReactorInterfaceHandler instance, Direction side, INBT nbt) {

            }
        }, ReactorInterfaceHandler::new);*/
    }
}
