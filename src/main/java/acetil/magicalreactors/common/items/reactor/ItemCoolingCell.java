package acetil.magicalreactors.common.items.reactor;

import net.minecraftforge.common.config.Configuration;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.lib.LibReactor;

public class ItemCoolingCell extends ItemReactor {

    public ItemCoolingCell (String registryName, int maxHeat) {
        super(maxHeat, 0, 0);
        setRegistryName(registryName);
        setUnlocalizedName(LibMisc.MODID + "." + registryName);
    }


    @Override
    public void readConfigs(Configuration config) {
        String category = getRegistryName().toString().split(":")[1];
        MAX_HEAT = config.getInt("max_heat", category, MAX_HEAT, 0, LibReactor.DEFAULT_MAX_HEAT,
                "Maximum heat the component can accept before melting down");
        setMaxDamage(MAX_HEAT);
    }
}
