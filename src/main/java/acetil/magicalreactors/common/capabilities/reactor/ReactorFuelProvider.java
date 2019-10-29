package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.capabilities.CapabilityReactorFuel;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReactorFuelProvider implements ICapabilitySerializable<INBT> {
    public IReactorFuelCapability capability;
    public ReactorFuelProvider (String name) {
        capability = new ReactorFuelCapability(name);
    }
    public ReactorFuelProvider () {
        capability = CapabilityReactorFuel.FUEL_CAPABILITY.getDefaultInstance();
    }
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityReactorFuel.FUEL_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityReactorFuel.FUEL_CAPABILITY ? CapabilityReactorFuel.FUEL_CAPABILITY.cast(this.capability) : null;
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().writeNBT(CapabilityReactorFuel.FUEL_CAPABILITY, capability, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityReactorFuel.FUEL_CAPABILITY.getStorage().readNBT(CapabilityReactorFuel.FUEL_CAPABILITY, capability, null, nbt);
    }
}
