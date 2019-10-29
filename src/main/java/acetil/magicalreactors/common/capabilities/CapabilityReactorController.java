package acetil.magicalreactors.common.capabilities;

import acetil.magicalreactors.common.capabilities.reactor.IReactorControlCapability;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactor.ReactorControlHandler;

import javax.annotation.Nullable;

public class CapabilityReactorController {
    @CapabilityInject(IReactorControlCapability.class)
    public static Capability<IReactorControlCapability> REACTOR_CONTROLLER;
    public static void register () {
        CapabilityManager.INSTANCE.register(IReactorControlCapability.class, new Capability.IStorage<IReactorControlCapability>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IReactorControlCapability> capability, IReactorControlCapability instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IReactorControlCapability> capability, IReactorControlCapability instance, Direction side, INBT nbt) {

            }
        }, ReactorControlHandler::new);
    }
}
