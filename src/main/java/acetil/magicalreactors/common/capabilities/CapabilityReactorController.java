package acetil.magicalreactors.common.capabilities;

import acetil.magicalreactors.common.capabilities.reactor.IReactorControlCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
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
            public NBTBase writeNBT(Capability<IReactorControlCapability> capability, IReactorControlCapability instance, EnumFacing side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IReactorControlCapability> capability, IReactorControlCapability instance, EnumFacing side, NBTBase nbt) {

            }
        }, ReactorControlHandler::new);
    }
}
