package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.capabilities.CapabilityReactorFuel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReactorFuelProvider implements ICapabilitySerializable<Tag> {
    public LazyOptional<IReactorFuelCapability> fuelOptional;
    public ReactorFuelProvider (String name) {
        fuelOptional = LazyOptional.of(() -> new ReactorFuelCapability(name));
    }
    public ReactorFuelProvider () {
        fuelOptional = LazyOptional.of(() -> new ReactorFuelCapability("default"));
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityReactorFuel.FUEL_CAPABILITY ? fuelOptional.cast() : LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        //return CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().writeNBT(CapabilityReactorFuel.FUEL_CAPABILITY, fuelOptional.orElse(CapabilityReactorFuel.FUEL_CAPABILITY.getDefaultInstance()), null);
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        //CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().readNBT(CapabilityReactorFuel.FUEL_CAPABILITY, fuelOptional.orElse(CapabilityReactorFuel.FUEL_CAPABILITY.getDefaultInstance()), null, nbt);
    }
}
