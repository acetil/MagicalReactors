package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MODID)
@SuppressWarnings("unchecked")
public class MachineBlocks {
    public static RegistryObject<BlockMachine> CENTRIFUGE = ModBlocks.BLOCKS.register("centrifuge", () -> new BlockMachine("centrifuge"));
    public static RegistryObject<BlockMachine> RECRYSTALISER = ModBlocks.BLOCKS.register("recrystaliser", () -> new BlockMachine("recrystaliser"));
    public static RegistryObject<BlockMachine> REACTOR_VESSEL = ModBlocks.BLOCKS.register("reactor_vessel", () -> new BlockMachine("reactor_vessel"));
    public static RegistryObject<BlockMachine> FERMENTER = ModBlocks.BLOCKS.register("fermenter", () -> new BlockMachine("fermenter"));
    public static RegistryObject<BlockMachine> CONDENSER = ModBlocks.BLOCKS.register("condenser", () -> new BlockMachine("condenser"));
    public static RegistryObject<BlockDistiller> DISTILLER = ModBlocks.BLOCKS.register("distiller", () -> new BlockDistiller("distiller", 2));
    public static RegistryObject<BlockMachine> DISSOLVER = ModBlocks.BLOCKS.register("dissolver", () -> new BlockMachine("dissolver"));

    private static RegistryObject<Item> CENTRIFUGE_ITEM = ModBlocks.ITEMS.register("centrifuge", () -> new BlockItem(CENTRIFUGE.get(), new Item.Properties()));
    private static RegistryObject<Item> RECRYSTALISER_ITEM = ModBlocks.ITEMS.register("recrystaliser", () -> new BlockItem(RECRYSTALISER.get(), new Item.Properties()));
    private static RegistryObject<Item> REACTOR_VESSEL_ITEM = ModBlocks.ITEMS.register("reactor_vessel", () -> new BlockItem(REACTOR_VESSEL.get(), new Item.Properties()));
    private static RegistryObject<Item> FERMENTER_ITEM = ModBlocks.ITEMS.register("fermenter", () -> new BlockItem(FERMENTER.get(), new Item.Properties()));
    private static RegistryObject<Item> CONDENSER_ITEM = ModBlocks.ITEMS.register("condenser", () -> new BlockItem(CONDENSER.get(), new Item.Properties()));
    private static RegistryObject<Item> DISTILLER_ITEM = ModBlocks.ITEMS.register("distiller", () -> new BlockItem(DISTILLER.get(), new Item.Properties()));
    private static RegistryObject<Item> DISSOLVER_ITEM = ModBlocks.ITEMS.register("dissolver", () -> new BlockItem(DISSOLVER.get(), new Item.Properties()));

    public static RegistryObject<TileEntityType<?>> MACHINE_BASE = ModBlocks.TILE_ENTITIES.register("machine_base",
            () -> ModBlocks.createTEType(TileMachineBase::new, CENTRIFUGE, RECRYSTALISER, FERMENTER, REACTOR_VESSEL, CONDENSER, DISSOLVER));
    public static RegistryObject<TileEntityType<?>> MACHINE_DISTILLER = ModBlocks.TILE_ENTITIES.register("machine_distiller",
            () -> ModBlocks.createTEType(TileMachineDistiller::new, DISTILLER));

    public static void registerMachines () {
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("centrifuge", 10000, 800, 400)
                                            .setInputSlots(1)
                                            .setOutputSlots(2)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("recrystaliser", 10000, 800, 400)
                                            .setInputSlots(1)
                                            .setOutputSlots(1)
                                            .setFluidInputSlots(1)
                                            .setFluidCapacity(4000)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("reactor_vessel", 10000, 800, 400)
                                            .setInputSlots(2)
                                            .setFluidInputSlots(1)
                                            .setFluidOutputSlots(1)
                                            .setFluidCapacity(4000)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("fermenter", 10000, 800, 400)
                                            .setInputSlots(1)
                                            .setFluidOutputSlots(1)
                                            .setFluidCapacity(4000)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("condenser", 10000, 800, 400)
                                            .setOutputSlots(1)
                                            .setFluidInputSlots(1)
                                            .setFluidCapacity(4000)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("distiller", 10000, 800, 400)
                                            .setFluidInputSlots(1)
                                            .setFluidOutputSlots(2)
                                            .setFluidCapacity(4000)
                                            .build());
        MachineRegistry.registerMachine(new MachineRegistryItem.Builder("dissolver", 10000, 800, 400)
                                            .setInputSlots(1)
                                            .setFluidInputSlots(1)
                                            .setFluidOutputSlots(2)
                                            .setFluidCapacity(4000)
                                            .build());
    }
}
