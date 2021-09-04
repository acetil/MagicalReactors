package acetil.magicalreactors.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactor.IReactorHandlerNew;
import acetil.magicalreactors.common.capabilities.reactor.ReactorHandlerNew;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public class CapabilityReactor {
    @CapabilityInject(IReactorHandlerNew.class)
    public static Capability<IReactorHandlerNew> CAPABILITY_REACTOR = null;
    public static void register (RegisterCapabilitiesEvent event) {
        event.register(IReactorHandlerNew.class);
        /*CapabilityManager.INSTANCE.register(IReactorHandlerNew.class, new Capability.IStorage<IReactorHandlerNew>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IReactorHandlerNew> capability, IReactorHandlerNew instance, Direction side) {
                // TODO: do nbt
                return null;
            }

            @Override
            public void readNBT(Capability<IReactorHandlerNew> capability, IReactorHandlerNew instance, Direction side, INBT nbt) {

            }
        }, ReactorHandlerNew::new);*/
    }
}
