package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.block.reactor.BlockReactorController;
import acetil.magicalreactors.common.block.reactor.BlockReactor;
import acetil.magicalreactors.common.block.reactor.BlockRuneBase;
import acetil.magicalreactors.common.machines.MachineBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = LibMisc.MODID)
@SuppressWarnings("unused")
@ObjectHolder(LibMisc.MODID)
public class ModBlocks {
    public static BlockOre TEMP_ORE1 = null; //TODO
    public static BlockOre TEMP_ORE2 = null; //TODO
    public static BlockReactorController REACTOR_CONTROLLER = null;
    public static BlockRuneBase RUNE_BASIC = null;
    public static BlockRuneBase RUNE_STABILISATION = null;
    public static BlockRuneBase RUNE_TRANSFER = null;
    public static BlockRuneBase RUNE_HARMONIC = null;
    public static BlockRuneBase RUNE_LOCUS = null;
    public static BlockReactor REACTOR_BLOCK = null;

    @SubscribeEvent
    public static void registerBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockOre("temp_ore1", 4f));
        event.getRegistry().register(new BlockOre("temp_ore2", 4f));
        event.getRegistry().register(new BlockReactorController());
        event.getRegistry().register(new BlockRuneBase("rune_basic"));
        event.getRegistry().register(new BlockRuneBase("rune_stabilisation"));
        event.getRegistry().register(new BlockRuneBase("rune_transfer"));
        event.getRegistry().register(new BlockRuneBase("rune_harmonic"));
        event.getRegistry().register(new BlockRuneBase("rune_locus"));
        event.getRegistry().register(new BlockReactor());
        MachineBlocks.registerMachineBlocks(event);
        /*GameRegistry.registerTileEntity(TileReactor.class, new ResourceLocation(LibMisc.MODID + ":reactor_entity"));
        GameRegistry.registerTileEntity(TileReactorController.class, new ResourceLocation(LibMisc.MODID + ":reactor_controller_entity"));
        GameRegistry.registerTileEntity(TileReactorNew.class, new ResourceLocation(LibMisc.MODID + ":reactor_entity_new"));*/
    }

    @SubscribeEvent
    public static void registerItems (RegistryEvent.Register<Item> event) {
        registerItemBlock(event, TEMP_ORE1);
        registerItemBlock(event, TEMP_ORE2);
        registerItemBlock(event, REACTOR_CONTROLLER);
        registerItemBlock(event, RUNE_BASIC);
        registerItemBlock(event, RUNE_STABILISATION);
        registerItemBlock(event, RUNE_TRANSFER);
        registerItemBlock(event, RUNE_HARMONIC);
        registerItemBlock(event, RUNE_LOCUS);
        registerItemBlock(event, REACTOR_BLOCK);
        MachineBlocks.registerMachineItems(event);
        System.out.println("Registered itemblocks");
    }
    public static void registerItemBlock (RegistryEvent.Register<Item> event, Block b) {
        event.getRegistry().register(new BlockItem(b, new Item.Properties()).setRegistryName(b.getRegistryName()));
    }
}
