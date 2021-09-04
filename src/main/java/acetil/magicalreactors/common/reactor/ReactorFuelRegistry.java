package acetil.magicalreactors.common.reactor;

import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import acetil.magicalreactors.common.capabilities.reactor.ReactorFuelProvider;
import acetil.magicalreactors.common.constants.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ReactorFuelRegistry {
    private static Map<String, Supplier<IReactorFuel>> fuels = new HashMap<>();
    private static Map<Item, String> itemFuels = new HashMap<>();
    private static void registerFuel (Supplier<IReactorFuel> fuel, String name) {
        fuels.put(name, fuel);
    }
    public static void registerFuel (Supplier<IReactorFuel> fuel, String name, Item item) {
        registerFuel(fuel, name);
        itemFuels.put(item, name);
    }
    public static IReactorFuel getFuel (String name) {
        return fuels.get(name).get();
    }


    public static void attachCapabilities (AttachCapabilitiesEvent<ItemStack> event) {
        if (itemFuels.containsKey(event.getObject().getItem())) {
            MagicalReactors.LOGGER.debug("Attaching capability!");
            event.addCapability(new ResourceLocation(Constants.MODID + ":reactor_fuel_capability"),
                    new ReactorFuelProvider(itemFuels.get(event.getObject().getItem())));
        }
    }
}
