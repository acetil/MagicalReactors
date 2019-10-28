package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.capabilities.ProviderReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.ReactorItem;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;

public class ItemHeatExchanger extends ItemReactor {
    public ItemHeatExchanger (String registryName, int heatTransferRate, int hullTransferRate,
                              int maxHeat, int coolingRate) {
        super(maxHeat, heatTransferRate, hullTransferRate);
        setRegistryName(registryName);
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
    }

    @Override
    public void readConfigs(Configuration config) {
        String category = getRegistryName().toString().split(":")[1];
        MAX_HEAT = config.getInt("max_heat", category, MAX_HEAT, 0, LibReactor.DEFAULT_MAX_HEAT,
                "Maximum heat the component can accept before melting down");
        HEAT_TRANSFER_RATE = config.getInt("heat_transfer_rate", category, HEAT_TRANSFER_RATE, 0, MAX_HEAT,
                "Controls the speed of side heat transfer");
        HULL_TRANSFER_RATE = config.getInt("hull_transfer_rate", category, HULL_TRANSFER_RATE, 0, MAX_HEAT,
                "Controls the speed of hull heat transfer");
        setMaxDamage(MAX_HEAT);
    }
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new ReactorItem().setMaxHeat(MAX_HEAT)
                .setHeatTransferRate(HEAT_TRANSFER_RATE)
                .setHullTransferRate(HULL_TRANSFER_RATE)
                .setIsSmart(true));
    }
}
