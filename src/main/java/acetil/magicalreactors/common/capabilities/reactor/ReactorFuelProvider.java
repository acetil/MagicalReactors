package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.capabilities.CapabilityReactorFuel;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReactorFuelProvider implements ICapabilitySerializable<INBT> {
    public LazyOptional<IReactorFuelCapability> fuelOptional;
    public ReactorFuelProvider (String name) {
        fuelOptional = LazyOptional.of(() -> new ReactorFuelCapability(name));
    }
    public ReactorFuelProvider () {
        fuelOptional = LazyOptional.of(CapabilityReactorFuel.FUEL_CAPABILITY::getDefaultInstance);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityReactorFuel.FUEL_CAPABILITY ? fuelOptional.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().writeNBT(CapabilityReactorFuel.FUEL_CAPABILITY, fuelOptional.orElse(CapabilityReactorFuel.FUEL_CAPABILITY.getDefaultInstance()), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().readNBT(CapabilityReactorFuel.FUEL_CAPABILITY, fuelOptional.orElse(CapabilityReactorFuel.FUEL_CAPABILITY.getDefaultInstance()), null, nbt);
    }
}
