package acetil.magicalreactors.common.items;

import acetil.magicalreactors.common.items.reactor.ReactorItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        ReactorItems.registerItems(event);
        MagicalReactors.LOGGER.log(Level.INFO, "Item registry complete");
    }
    @SideOnly(Side.CLIENT)
    public static void initModels () {
        URANIUM_INGOT.initModel();
        ITEM_TEMP2.initModel();
        ReactorItems.initModels();

        MagicalReactors.LOGGER.log(Level.INFO, "Item models initialised");
    }

}
