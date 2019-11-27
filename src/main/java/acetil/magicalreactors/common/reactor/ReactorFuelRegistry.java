package acetil.magicalreactors.common.reactor;

import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import acetil.magicalreactors.common.capabilities.reactor.ReactorFuelProvider;
import acetil.magicalreactors.common.lib.LibMisc;

import java.util.HashMap;
import java.util.Map;
@Mod.EventBusSubscriber
public class ReactorFuelRegistry {
    private static Map<String, IReactorFuel> fuels = new HashMap<>();
    private static Map<Item, String> itemFuels = new HashMap<>();
    private static void registerFuel (IReactorFuel fuel) {
        fuels.put(fuel.getName(), fuel);
    }
    public static void registerFuel (IReactorFuel fuel, Item item) {
        registerFuel(fuel);
        itemFuels.put(item, fuel.getName());
    }
    public static IReactorFuel getFuel (String name) {
        return fuels.get(name);
    }


    @SubscribeEvent
    public static void attachCapabilities (AttachCapabilitiesEvent<ItemStack> event) {
        if (itemFuels.containsKey(event.getObject().getItem())) {
            MagicalReactors.LOGGER.debug("Attaching capability!");
            event.addCapability(new ResourceLocation(LibMisc.MODID + ":reactor_fuel_capability"),
                    new ReactorFuelProvider(itemFuels.get(event.getObject().getItem())));
        }
    }
    @SubscribeEvent
    public static void blockBreakEvent (BlockEvent.BreakEvent event) {
        MagicalReactors.LOGGER.debug("Event test!");
    }
}
