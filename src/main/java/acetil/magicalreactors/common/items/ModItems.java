package acetil.magicalreactors.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.lib.LibMisc;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = LibMisc.MODID)
public class ModItems {

    public static ItemResource URANIUM_INGOT = new ItemResource("uranium_ingot");
    public static ItemResource ITEM_TEMP2 = new ItemResource("temp2");
    @SubscribeEvent
    public static void registerItems (RegistryEvent.Register<Item> event) {
        event.getRegistry().register(URANIUM_INGOT);
        event.getRegistry().register(ITEM_TEMP2);
        MagicalReactors.LOGGER.log(Level.INFO, "Item registry complete");
    }

}
