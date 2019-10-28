package acetil.magicalreactors.common.items.reactor;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;

public class ReactorItems {
    public static ItemReactor[] reactorItems = {
            new ItemHeatVent("vent_basic", 5, 5, 1000),
            new ItemFuelRod("uranium_rod", 40, 2, 2000, 65535, 0),
            new ItemHeatExchanger("exchanger_basic", 12, 4, 1000, 0),
            new ItemCoolingCell("coolingcell_10k", 10000),
            new ItemNeutronDeflector("deflector_basic", 20000, 2000)
    };

    public static void readConfigs (Configuration config) {
        for (ItemReactor item : reactorItems) {
            item.readConfigs(config);
        }
    }

    public static void registerItems (RegistryEvent.Register<Item> event) {
        for (ItemReactor item : reactorItems) {
            event.getRegistry().register(item);
        }
    }

    public static void initModels () {
        for (ItemReactor item : reactorItems) {
            item.initModel();
        }
    }
}
