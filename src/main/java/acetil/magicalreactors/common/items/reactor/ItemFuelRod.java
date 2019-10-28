package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.capabilities.ProviderReactorItem;
import acetil.magicalreactors.common.capabilities.reactoritems.RodReactorItem;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;

public class ItemFuelRod extends ItemReactor {
    protected int ENERGY_PER_PULSE;
    protected int HEAT_COEFFICIENT;
    protected int MAX_DAMAGE;
    protected int DEFAULT_EFFICIENCY;
    int pulses;
    int efficiency;
    public ItemFuelRod (String registryName, int energyPerPulse, int heatCoefficient, int maxHeat, int maxDamage,
                        int defaultEfficiency) {
        super(maxHeat, maxHeat, -1 * maxHeat);
        ENERGY_PER_PULSE = energyPerPulse;
        HEAT_COEFFICIENT = heatCoefficient;
        MAX_DAMAGE = maxDamage;
        DEFAULT_EFFICIENCY = defaultEfficiency;
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
        setRegistryName(registryName);
    }

    @Override
    public void readConfigs(Configuration config) {
        String category = getRegistryName().toString().split(":")[1];
        MAX_HEAT = config.getInt("max_heat", category, MAX_HEAT, 0, LibReactor.DEFAULT_MAX_HEAT,
                "Maximum heat the component can accept before melting down");
        MAX_DAMAGE = config.getInt("max_damage", category, MAX_DAMAGE, 0, 65535,
                "How long a fuel rod can go before being consumed");
        ENERGY_PER_PULSE = config.getInt("energy_per_pulse", category, ENERGY_PER_PULSE, 0, 65535 / 4,
                "The amount of energy produced per neutron pulse");
        HEAT_COEFFICIENT = config.getInt("heat_coefficient", category, HEAT_COEFFICIENT, 0, MAX_HEAT,
                "The coefficient of the heat function: h = kn(n+2), where n is the efficiency");
        DEFAULT_EFFICIENCY = config.getInt("default_efficiency", category, DEFAULT_EFFICIENCY, 0, 1000,
                "The default efficiency the fuel rod has BEFORE the first pulse");
        setMaxDamage(MAX_DAMAGE);
        System.out.println("Fuel rod max damage: " + MAX_DAMAGE);
    }
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ProviderReactorItem(new RodReactorItem().setEnergyPerPulse(ENERGY_PER_PULSE)
                .setHeatCoefficient(HEAT_COEFFICIENT)
                .setDefaultEfficiency(DEFAULT_EFFICIENCY)
                .setDamage(stack.getItemDamage())
                .setMaxHeat(MAX_HEAT)
        .setIsSmart(false));
    }
}
