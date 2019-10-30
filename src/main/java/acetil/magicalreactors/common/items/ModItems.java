package acetil.magicalreactors.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = LibMisc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MODID)
public class ModItems {

    public static ItemResource URANIUM_INGOT = null;
    public static ItemResource ITEM_TEMP2 = null;
    @SubscribeEvent
    public static void registerItems (RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemResource("uranium_ingot"));
        event.getRegistry().register(new ItemResource("temp2"));
        MagicalReactors.LOGGER.log(Level.INFO, "Item registry complete");
    }

}
