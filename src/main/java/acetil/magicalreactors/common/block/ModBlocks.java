package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.reactor.*;
import acetil.magicalreactors.common.fluid.ModFluids;
import acetil.magicalreactors.common.machines.MachineBlocks;
import acetil.magicalreactors.common.tiles.*;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LibMisc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
@ObjectHolder(LibMisc.MODID)
public class ModBlocks {
    // TODO: move to DefferedLoader
    // TODO: make config values suppliers / add reload support
    // TODO: cleanup
    @ObjectHolder("temp_ore")
    public static Block TEMP_ORE1 = null; //TODO
    @ObjectHolder("temp_ore_b")
    public static Block TEMP_ORE2 = null; //TODO
    @ObjectHolder("reactor_controller")
    public static Block REACTOR_CONTROLLER = null;
    @ObjectHolder("rune_basic")
    public static Block RUNE_BASIC = null;
    @ObjectHolder("rune_stabilisation")
    public static Block RUNE_STABILISATION = null;
    @ObjectHolder("rune_transfer")
    public static Block RUNE_TRANSFER = null;
    @ObjectHolder("rune_harmonic")
    public static Block RUNE_HARMONIC = null;
    @ObjectHolder("rune_locus")
    public static Block RUNE_LOCUS = null;
    @ObjectHolder("reactor_block")
    public static Block REACTOR_BLOCK = null;
    @ObjectHolder("test_energy_source")
    public static Block TEST_ENERGY_SOURCE = null;
    @ObjectHolder("fuel_loader")
    public static Block FUEL_INTERFACE = null;
    @ObjectHolder("redstone_interface")
    public static Block REDSTONE_INTERFACE = null;
    @ObjectHolder("energy_interface")
    public static Block ENERGY_INTERFACE = null;
    @ObjectHolder("cooling_interface")
    public static Block COOLING_INTERFACE = null;
    @ObjectHolder("ethanol_block")
    public static FlowingFluidBlock ETHANOL_BLOCK = null;
    @ObjectHolder("test_battery")
    public static Block TEST_BATTERY = null;
    @ObjectHolder("reactor")
    public static TileEntityType<?> REACTOR_TILE_ENTITY = null;
    @ObjectHolder("reactor_controller")
    public static TileEntityType<?> REACTOR_CONTROLLER_TILE_ENTITY = null;
    @ObjectHolder("test_energy_source")
    public static TileEntityType<?> TEST_ENERGY_SOURCE_TILE = null;
    public static TileEntityType<?> BYPRODUCT_INTERFACE;
    @ObjectHolder("cooling_interface_tile")
    public static TileEntityType<?> COOLING_INTERFACE_TILE;
    @ObjectHolder("energy_interface_tile")
    public static TileEntityType<?> ENERGY_INTERFACE_TILE;
    @ObjectHolder("fuel_interface_tile")
    public static TileEntityType<?> FUEL_INTERFACE_TILE;
    @ObjectHolder("redstone_interface_tile")
    public static TileEntityType<?> REDSTONE_INTERFACE_TILE = null;
    @ObjectHolder("test_battery_tile")
    public static TileEntityType<?> TEST_BATTERY_TILE = null;
    @SubscribeEvent
    public static void registerBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockOre("temp_ore", 4f));
        event.getRegistry().register(new BlockOre("temp_ore_b", 4f));
        event.getRegistry().register(new BlockReactorController());
        event.getRegistry().register(new BlockRuneBase("rune_basic"));
        event.getRegistry().register(new BlockRuneBase("rune_stabilisation"));
        event.getRegistry().register(new BlockRuneBase("rune_transfer"));
        event.getRegistry().register(new BlockRuneBase("rune_harmonic"));
        event.getRegistry().register(new BlockRuneBase("rune_locus"));
        event.getRegistry().register(new BlockReactor());
        event.getRegistry().register(new BlockReactorInterface("fuel_loader",
                TileReactorInterfaceFuelLoader::new));
        event.getRegistry().register(new BlockReactorInterfaceRedstone("redstone_interface",
                TileReactorInterfaceRedstone::new));
        event.getRegistry().register(new BlockReactorInterface("energy_interface",
                () -> new TileReactorInterfaceEnergy(100000, 1000)));
        event.getRegistry().register(new BlockReactorInterface("cooling_interface",
                TileReactorInterfaceCooling::new));
        event.getRegistry().register(new BlockTestEnergySource());
        event.getRegistry().register(new FlowingFluidBlock(() -> ModFluids.STILL_ETHANOL,
                Block.Properties.create(Material.WATER).doesNotBlockMovement().noDrops()).setRegistryName("ethanol_block"));
        event.getRegistry().register(new BlockTestBattery());
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
        registerItemBlock(event, TEST_ENERGY_SOURCE);
        registerItemBlock(event, ETHANOL_BLOCK); //TODO: remove
        registerItemBlock(event, FUEL_INTERFACE);
        registerItemBlock(event, REDSTONE_INTERFACE);
        registerItemBlock(event, ENERGY_INTERFACE);
        registerItemBlock(event, COOLING_INTERFACE);
        registerItemBlock(event, TEST_BATTERY);
        MachineBlocks.registerMachineItems(event);
        System.out.println("Registered itemblocks");
    }
    public static void registerItemBlock (RegistryEvent.Register<Item> event, Block b) {
        MagicalReactors.LOGGER.info("Registered itemblock for {}", b.getRegistryName());
        event.getRegistry().register(new BlockItem(b, new Item.Properties()).setRegistryName(b.getRegistryName()));
    }
    @SubscribeEvent
    public static void registerTileEntities (RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity(event, TileReactor::new, "reactor", REACTOR_BLOCK);
        registerTileEntity(event, TileReactorController::new, "reactor_controller", REACTOR_CONTROLLER);
        registerTileEntity(event, TileReactorInterfaceByproduct::new, "byproduct_interface");
        registerTileEntity(event, TileReactorInterfaceCooling::new, "cooling_interface_tile");
        registerTileEntity(event, TileReactorInterfaceEnergy::new, "energy_interface_tile", ENERGY_INTERFACE);
        registerTileEntity(event, TileReactorInterfaceFuelLoader::new, "fuel_interface_tile", FUEL_INTERFACE);
        registerTileEntity(event, TileTestEnergySource::new, "test_energy_source", TEST_ENERGY_SOURCE);
        registerTileEntity(event, TileReactorInterfaceRedstone::new, "redstone_interface_tile", REDSTONE_INTERFACE);
        registerTileEntity(event, TileTestBattery::new, "test_battery_tile", TEST_BATTERY);
        MachineBlocks.registerTileEntities(event);
        MagicalReactors.LOGGER.info("Registered tile entities!");
    }
    public static void registerTileEntity (RegistryEvent.Register<TileEntityType<?>> event, Supplier<? extends TileEntity> factory,
                                           String name, Block... blocks) {
        event.getRegistry().register(TileEntityType.Builder.create(factory, blocks).build(null)
                .setRegistryName(new ResourceLocation(LibMisc.MODID, name)));
    }
}
