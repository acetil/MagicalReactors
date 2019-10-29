package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.capabilities.reactoritems.AdvVentReactorItem;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;

public class ItemAdvancedVent extends ItemReactor {
    protected int MAX_RESOURCE;
    protected int RESOURCE_PER_ITEM;
    protected int resource;
    public ItemAdvancedVent (String registryName, int heatTransferRate, int hullTransferRate, int maxHeat,
                             int maxResource, int resourcePerItem) {
        super(maxHeat, heatTransferRate, hullTransferRate);
        MAX_RESOURCE = maxResource;
        RESOURCE_PER_ITEM = resourcePerItem;
        resource = maxResource;
        setRegistryName(registryName);
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
        setMaxDamage(maxResource);
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
        MAX_RESOURCE = config.getInt("max_resource", category, MAX_RESOURCE, 0, MAX_HEAT,
                "The maximum heat this component can accept full of resource");
        RESOURCE_PER_ITEM = config.getInt("resource_per_item", category, RESOURCE_PER_ITEM, 0, MAX_RESOURCE,
                "The amount of resource one item gives");
        setMaxDamage(MAX_RESOURCE);

    }
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new AdvVentReactorItem().setDamage(stack.getItemDamage())
                .setMaxHeat(MAX_HEAT)
                .setHeatTransferRate(HEAT_TRANSFER_RATE)
                .setHullTransferRate(HULL_TRANSFER_RATE)
                .setIsSmart(false));
    }
}
