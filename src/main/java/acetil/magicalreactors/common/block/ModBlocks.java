package acetil.magicalreactors.common.block;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.reactor.*;
import acetil.magicalreactors.common.fluid.FluidEthanol;
import acetil.magicalreactors.common.fluid.ModFluids;
import acetil.magicalreactors.common.machines.MachineBlocks;
import acetil.magicalreactors.common.tiles.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Arrays;

@SuppressWarnings({"unused", "unchecked"})
@ObjectHolder(Constants.MODID)
public class ModBlocks {
    // TODO: make config values suppliers / add reload support
    // TODO: cleanup
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    public static RegistryObject<Block> TEMP_ORE1 = BLOCKS.register("temp_ore", () ->
            new BlockOre(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> TEMP_ORE2 = BLOCKS.register("temp_ore_b",
            () -> new BlockOre(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> REACTOR_CONTROLLER = BLOCKS.register("reactor_controller",
            () -> new BlockReactorController(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> RUNE_BASIC = BLOCKS.register("rune_basic",
            () -> new BlockRuneBase(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> RUNE_STABILISATION = BLOCKS.register("rune_stabilisation",
            () -> new BlockRuneBase(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> RUNE_TRANSFER = BLOCKS.register("rune_transfer",
            () -> new BlockRuneBase(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> RUNE_HARMONIC = BLOCKS.register("rune_harmonic",
            () -> new BlockRuneBase(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> RUNE_LOCUS = BLOCKS.register("rune_locus",
            () -> new BlockRuneBase(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> REACTOR_BLOCK = BLOCKS.register("reactor_block",
            () -> new BlockReactor(Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> FUEL_INTERFACE = BLOCKS.register("fuel_loader",
            () -> new BlockReactorInterface(TileReactorInterfaceFuelLoader::new, Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> REDSTONE_INTERFACE = BLOCKS.register("redstone_interface",
            () -> new BlockReactorInterfaceRedstone(TileReactorInterfaceRedstone::new, Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> ENERGY_INTERFACE = BLOCKS.register("energy_interface",
            () -> new BlockReactorInterface(TileReactorInterfaceEnergy::new, Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> BYPRODUCT_INTERFACE = BLOCKS.register("byproduct_interface",
            () -> new BlockReactorInterface(TileReactorInterfaceByproduct::new, Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> COOLING_INTERFACE = BLOCKS.register("cooling_interface",
            () -> new BlockReactorInterface(TileReactorInterfaceCooling::new, Block.Properties.of(Material.STONE)));
    public static RegistryObject<Block> TEST_ENERGY_SOURCE = BLOCKS.register("test_energy_source", BlockTestEnergySource::new);
    public static RegistryObject<Block> TEST_BATTERY = BLOCKS.register("test_battery", BlockTestBattery::new);
    public static RegistryObject<Block> ETHANOL_BLOCK = BLOCKS.register("ethanol_block", () -> new LiquidBlock(() -> (FlowingFluid) ModFluids.STILL_ETHANOL.get(),
            Block.Properties.of(Material.WATER).noCollission().noDrops()));

    private static RegistryObject<Item> TEMP_ORE1_ITEM = ITEMS.register("temp_ore", () -> new BlockItem(TEMP_ORE1.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> TEMP_ORE2_ITEM = ITEMS.register("temp_ore_b", () -> new BlockItem(TEMP_ORE2.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> REACTOR_CONTROLLER_ITEM = ITEMS.register("reactor_controller", () -> new BlockItem(REACTOR_CONTROLLER.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> RUNE_BASIC_ITEM = ITEMS.register("rune_basic", () -> new BlockItem(RUNE_BASIC.get(), new Item.Properties()));
    private static RegistryObject<Item> RUNE_STABILISATION_ITEM = ITEMS.register("rune_stabilisation", () -> new BlockItem(RUNE_STABILISATION.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> RUNE_TRANSFER_ITEM = ITEMS.register("rune_transfer", () -> new BlockItem(RUNE_TRANSFER.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> RUNE_HARMONIC_ITEM = ITEMS.register("rune_harmonic", () -> new BlockItem(RUNE_HARMONIC.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> RUNE_LOCUS_ITEM = ITEMS.register("rune_locus", () -> new BlockItem(RUNE_LOCUS.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> REACTOR_BLOCK_ITEM = ITEMS.register("reactor_block", () -> new BlockItem(REACTOR_BLOCK.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> FUEL_INTERFACE_ITEM = ITEMS.register("fuel_interface", () -> new BlockItem(FUEL_INTERFACE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> REDSTONE_INTERFACE_ITEM = ITEMS.register("redstone_interface", () -> new BlockItem(REDSTONE_INTERFACE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> ENERGY_INTERFACE_ITEM = ITEMS.register("energy_interface", () -> new BlockItem(ENERGY_INTERFACE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> BYPRODUCT_INTERFACE_ITEM = ITEMS.register("byproduct_interface", () -> new BlockItem(BYPRODUCT_INTERFACE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> COOLING_INTERFACE_ITEM = ITEMS.register("cooling_interface", () -> new BlockItem(COOLING_INTERFACE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> TEST_ENERGY_SOURCE_ITEM = ITEMS.register("test_energy_source", () -> new BlockItem(TEST_ENERGY_SOURCE.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));
    private static RegistryObject<Item> TEST_BATTERY_ITEM = ITEMS.register("test_battery", () -> new BlockItem(TEST_BATTERY.get(), new Item.Properties().tab(MagicalReactors.ITEM_GROUP)));

    public static RegistryObject<BlockEntityType<?>> REACTOR_TILE_ENTITY = TILE_ENTITIES.register("reactor", () -> createTEType(TileReactor::new, REACTOR_BLOCK));
    public static RegistryObject<BlockEntityType<?>> REACTOR_CONTROLLER_TILE = TILE_ENTITIES.register("reactor_controller", () -> createTEType(TileReactorController::new, REACTOR_CONTROLLER));
    public static RegistryObject<BlockEntityType<?>> BYPRODUCT_INTERFACE_TILE = TILE_ENTITIES.register("byproduct_interface", () -> createTEType(TileReactorInterfaceByproduct::new, BYPRODUCT_INTERFACE));
    public static RegistryObject<BlockEntityType<?>> COOLING_INTERFACE_TILE = TILE_ENTITIES.register("cooling_interface", () -> createTEType(TileReactorInterfaceCooling::new, COOLING_INTERFACE));
    public static RegistryObject<BlockEntityType<?>> ENERGY_INTERFACE_TILE = TILE_ENTITIES.register("energy_interface", () -> createTEType(TileReactorInterfaceEnergy::new, ENERGY_INTERFACE));
    public static RegistryObject<BlockEntityType<?>> FUEL_INTERFACE_TILE = TILE_ENTITIES.register("fuel_interface", () -> createTEType(TileReactorInterfaceFuelLoader::new, FUEL_INTERFACE));
    public static RegistryObject<BlockEntityType<?>> TEST_ENERGY_SOURCE_TILE = TILE_ENTITIES.register("test_energy_source", () -> createTEType(TileTestEnergySource::new, TEST_ENERGY_SOURCE));
    public static RegistryObject<BlockEntityType<?>> REDSTONE_INTERFACE_TILE = TILE_ENTITIES.register("redstone_interface", () -> createTEType(TileReactorInterfaceRedstone::new, REDSTONE_INTERFACE));
    public static RegistryObject<BlockEntityType<?>> TEST_BATTERY_TILE = TILE_ENTITIES.register("test_battery", () -> createTEType(TileTestBattery::new, TEST_BATTERY));

    public static BlockEntityType<?> createTEType (BlockEntityType.BlockEntitySupplier<? extends BlockEntity> factory, RegistryObject<? extends Block>... blocks) {
        return BlockEntityType.Builder.of(factory, (Block[])Arrays.stream(blocks).map(RegistryObject::get).toArray(Block[]::new)).build(null);
    }
}
