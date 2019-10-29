package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.capabilities.reactoritems.DeflectorReactorItem;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;

public class ItemNeutronDeflector extends ItemReactor {
    protected int MAX_DAMAGE;
    int damage;
    public ItemNeutronDeflector (String registryName, int maxDamage, int maxHeat) {
        super(maxHeat, 0, 0);
        MAX_DAMAGE = maxDamage;
        MAX_HEAT = maxHeat;
        setRegistryName(registryName);
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
        damage = 0;
    }

    @Override
    public void readConfigs(Configuration config) {
        String category = getRegistryName().toString().split(":")[1];
        MAX_HEAT = config.getInt("max_heat", category, MAX_HEAT, 0, LibReactor.DEFAULT_MAX_HEAT,
                "Maximum heat the component can accept before melting down");
        MAX_DAMAGE = config.getInt("max_pulses", category, MAX_DAMAGE, 0, 2147483647,
                "The amount of pulses before the deflector breaks");
        setMaxDamage(MAX_DAMAGE);
    }
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new DeflectorReactorItem().setDamage(stack.getItemDamage())
                .setMaxHeat(MAX_HEAT)
                .setHeatTransferRate(HEAT_TRANSFER_RATE)
                .setHullTransferRate(HULL_TRANSFER_RATE));
    }
}
