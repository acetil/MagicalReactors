package acetil.magicalreactors.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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
            public NBTBase writeNBT(Capability<IReactorFuelCapability> capability, IReactorFuelCapability instance, EnumFacing side) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("name", instance.getName());
                return nbt;
            }

            @Override
            public void readNBT(Capability<IReactorFuelCapability> capability, IReactorFuelCapability instance, EnumFacing side, NBTBase nbt) {
                instance.setName(((NBTTagCompound)nbt).getString("name"));
            }
        }, () -> new ReactorFuelCapability("default")); //TODO: update
    }
}
