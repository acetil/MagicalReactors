package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.capabilities.ProviderReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.VentReactorItem;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;

public class ItemHeatVent extends ItemReactor {
    protected int COOLING_RATE;
    public ItemHeatVent (String registryName, int hullTransferRate, int coolingRate, int maxHeat) {
        super(maxHeat, 0, hullTransferRate);
        HULL_TRANSFER_RATE = hullTransferRate;
        COOLING_RATE = coolingRate;
        MAX_HEAT = maxHeat;
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
        setRegistryName(registryName);
        setMaxDamage(maxHeat);
    }

    @Override
    public void readConfigs(Configuration config) {
        String category = getRegistryName().toString().split(":")[1];
        MAX_HEAT = config.getInt("max_heat", category, MAX_HEAT, 0, LibReactor.DEFAULT_MAX_HEAT,
                "Maximum heat the component can accept before melting down");
        HULL_TRANSFER_RATE = config.getInt("hull_transfer_rate", category, HULL_TRANSFER_RATE, 0, MAX_HEAT,
                "Controls the speed of hull heat transfer");
        COOLING_RATE = config.getInt("cooling_rate", category, COOLING_RATE, 0, MAX_HEAT,
                "Controls the rate of cooling");
        setMaxDamage(MAX_HEAT);
    }
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new VentReactorItem().setCoolingRate(COOLING_RATE)
                .setMaxHeat(MAX_HEAT)
                .setHeatTransferRate(HEAT_TRANSFER_RATE)
                .setHullTransferRate(HULL_TRANSFER_RATE));
    }
}
