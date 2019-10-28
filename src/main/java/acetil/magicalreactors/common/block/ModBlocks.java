package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.block.reactor.BlockReactorController;
import acetil.magicalreactors.common.block.reactor.BlockReactorNew;
import acetil.magicalreactors.common.block.reactor.BlockRuneBase;
import acetil.magicalreactors.common.machines.MachineBlocks;
import acetil.magicalreactors.common.tiles.TileReactor;
import acetil.magicalreactors.common.tiles.TileReactorController;
import acetil.magicalreactors.common.tiles.TileReactorNew;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MODID)
@SuppressWarnings("unused")
public class ModBlocks {
    public static BlockUranium BLOCK_URANIUM = new BlockUranium();
    public static BlockReactor BLOCK_REACTOR = new BlockReactor();
    public static BlockOre TEMP_ORE1 = new BlockOre("temp_ore1", 4f); //TODO
    public static BlockOre TEMP_ORE2 = new BlockOre("temp_ore2", 4f); //TODO
    public static BlockReactorController REACTOR_CONTROLLER = new BlockReactorController();
    public static BlockRuneBase RUNE_BASIC = new BlockRuneBase("rune_basic");
    public static BlockRuneBase RUNE_STABILISATION = new BlockRuneBase("rune_stabilisation");
    public static BlockRuneBase RUNE_TRANSFER = new BlockRuneBase("rune_transfer");
    public static BlockRuneBase RUNE_HARMONIC = new BlockRuneBase("rune_harmonic");
    public static BlockRuneBase RUNE_LOCUS = new BlockRuneBase("rune_locus");
    public static BlockReactorNew REACTOR_NEW = new BlockReactorNew();

    @SubscribeEvent
    public static void registerBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(BLOCK_URANIUM);
        event.getRegistry().register(BLOCK_REACTOR);
        event.getRegistry().register(TEMP_ORE1);
        event.getRegistry().register(TEMP_ORE2);
        event.getRegistry().register(REACTOR_CONTROLLER);
        event.getRegistry().register(RUNE_BASIC);
        event.getRegistry().register(RUNE_STABILISATION);
        event.getRegistry().register(RUNE_TRANSFER);
        event.getRegistry().register(RUNE_HARMONIC);
        event.getRegistry().register(RUNE_LOCUS);
        event.getRegistry().register(REACTOR_NEW);
        MachineBlocks.registerMachineBlocks(event);
        GameRegistry.registerTileEntity(TileReactor.class, new ResourceLocation(LibMisc.MODID + ":reactor_entity"));
        GameRegistry.registerTileEntity(TileReactorController.class, new ResourceLocation(LibMisc.MODID + ":reactor_controller_entity"));
        GameRegistry.registerTileEntity(TileReactorNew.class, new ResourceLocation(LibMisc.MODID + ":reactor_entity_new"));
    }

    @SubscribeEvent
    public static void registerItems (RegistryEvent.Register<Item> event) {
        registerItemBlock(event, BLOCK_URANIUM);
        registerItemBlock(event, BLOCK_REACTOR);
        registerItemBlock(event, TEMP_ORE1);
        registerItemBlock(event, TEMP_ORE2);
        registerItemBlock(event, REACTOR_CONTROLLER);
        registerItemBlock(event, RUNE_BASIC);
        registerItemBlock(event, RUNE_STABILISATION);
        registerItemBlock(event, RUNE_TRANSFER);
        registerItemBlock(event, RUNE_HARMONIC);
        registerItemBlock(event, RUNE_LOCUS);
        registerItemBlock(event, REACTOR_NEW);
        MachineBlocks.registerMachineItems(event);
        System.out.println("Registered itemblocks");
    }
    @SideOnly(Side.CLIENT)
    public static void initModels () {
        BLOCK_URANIUM.initModel();
        BLOCK_REACTOR.initModel();
        TEMP_ORE1.initModel();
        TEMP_ORE2.initModel();
        REACTOR_CONTROLLER.initModel();
        RUNE_BASIC.initModel();
        RUNE_STABILISATION.initModel();
        RUNE_TRANSFER.initModel();
        RUNE_HARMONIC.initModel();
        RUNE_LOCUS.initModel();
        REACTOR_NEW.initModel();
        MachineBlocks.initMachineModels();
    }
    public static void registerItemBlock (RegistryEvent.Register<Item> event, Block b) {
        event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
    }
}
