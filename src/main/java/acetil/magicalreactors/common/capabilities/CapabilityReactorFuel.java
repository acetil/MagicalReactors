package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import acetil.magicalreactors.common.capabilities.reactor.IReactorFuelCapability;
import acetil.magicalreactors.common.capabilities.reactor.ReactorFuelCapability;

import javax.annotation.Nullable;

public class CapabilityReactorFuel {
    @CapabilityInject(IReactorFuelCapability.class)
    public static Capability<IReactorFuelCapability> FUEL_CAPABILITY = null;
    public static void register () {
        CapabilityManager.INSTANCE.register(IReactorFuelCapability.class, new Capability.IStorage<IReactorFuelCapability>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IReactorFuelCapability> capability, IReactorFuelCapability instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putString("name", instance.getName());
                return nbt;
            }

            @Override
            public void readNBT(Capability<IReactorFuelCapability> capability, IReactorFuelCapability instance, Direction side, INBT nbt) {
                instance.setName(((CompoundNBT)nbt).getString("name"));
            }
        }, () -> new ReactorFuelCapability("default")); //TODO: update
    }
}
