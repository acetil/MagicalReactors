package acetil.magicalreactors.common.items;

import acetil.magicalreactors.common.reactor.ReactorCoolingRegistry;
import acetil.magicalreactors.common.reactor.ReactorFuelBasic;
import acetil.magicalreactors.common.reactor.ReactorFuelRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Level;

@ObjectHolder(Constants.MODID)
public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", () -> new Item(new Item.Properties().tab((MagicalReactors.ITEM_GROUP))));
    public static RegistryObject<Item> ITEM_TEMP2 = ITEMS.register("temp2", () -> new Item(new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    public static void registerFuels () {
        ReactorFuelRegistry.registerFuel(ReactorFuelBasic::new, "basic_fuel", URANIUM_INGOT.get());
    }
    public static void registerCoolants () {
        ReactorCoolingRegistry.registerCooling("minecraft:ice", 100);
    }
}
