package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(LibMisc.MODID)
public class MachineBlocks {
    @ObjectHolder("centrifuge")
    public static BlockMachine CENTRIFUGE = null;
    @ObjectHolder("recrystaliser")
    public static BlockMachine RECRYSTALISER = null;
    @ObjectHolder("reactor_vessel")
    public static BlockMachine REACTOR_VESSEL = null;
    @ObjectHolder("fermenter")
    public static BlockMachine FERMENTER = null;
    @ObjectHolder("condenser")
    public static BlockMachine CONDENSER = null;
    @ObjectHolder("distiller")
    public static BlockDistiller DISTILLER = null;
    @ObjectHolder("machine_base")
    public static TileEntityType<?> MACHINE_BASE = null;
    @ObjectHolder("machine_distiller")
    public static TileEntityType<?> MACHINE_DISTILLER = null;

    public static void registerMachineBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockMachine("centrifuge", "centrifuge"));
        event.getRegistry().register(new BlockMachine("recrystaliser", "recrystaliser"));
        event.getRegistry().register(new BlockMachine("reactor_vessel", "reactor_vessel"));
        event.getRegistry().register(new BlockMachine("fermenter", "fermenter"));
        event.getRegistry().register(new BlockMachine("condenser", "condenser"));
        event.getRegistry().register(new BlockDistiller("distiller", "distiller", 2));

        /*GameRegistry.registerTileEntity(TileMachineBase.class, new ResourceLocation(LibMisc.MODID + ":machine_entity"));
        GameRegistry.registerTileEntity(TileMachineDistiller.class, new ResourceLocation(LibMisc.MODID + ":distiller_entity"));*/

        registerMachines();
    }
    public static void registerMachines () {
        // TODO: Potentially change to builder pattern
        MachineRegistry.registerMachine(new MachineRegistryItem("centrifuge", 1, 2, 10000,
                LibGui.TEST_GUI_ID, 800, 400));
        MachineRegistry.registerMachine(new MachineRegistryItem("recrystaliser", 1, 1,
                10000, LibGui.TEST_GUI_ID,800, 400,
                true, 1, 0, 4000));
        MachineRegistry.registerMachine(new MachineRegistryItem("reactor_vessel", 2, 0, 10000,
                LibGui.TEST_GUI_ID, 800, 400, true, 1, 1, 4000));
        MachineRegistry.registerMachine(new MachineRegistryItem("fermenter", 1, 0, 10000,
                LibGui.TEST_GUI_ID, 800, 400, true, 0, 1, 4000));
        MachineRegistry.registerMachine(new MachineRegistryItem("rod_filler", 2, 1, 10000,
                LibGui.TEST_GUI_ID, 800, 400));
        MachineRegistry.registerMachine(new MachineRegistryItem("condenser", 0, 1, 10000,
                LibGui.TEST_GUI_ID, 800, 400, true, 1, 0, 4000));
        MachineRegistry.registerMachine(new MachineRegistryItem("distiller", 0, 0, 10000,
                LibGui.TEST_GUI_ID, 800, 400, true , 1, 2, 4000));
    }
    public static void registerMachineItems (RegistryEvent.Register<Item> event) {
        ModBlocks.registerItemBlock(event, CENTRIFUGE);
        ModBlocks.registerItemBlock(event, RECRYSTALISER);
        ModBlocks.registerItemBlock(event, REACTOR_VESSEL);
        ModBlocks.registerItemBlock(event, FERMENTER);
        ModBlocks.registerItemBlock(event, CONDENSER);
        ModBlocks.registerItemBlock(event, DISTILLER);
    }
    public static void registerTileEntities (RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder
                .create(TileMachineBase::new, CENTRIFUGE, RECRYSTALISER, FERMENTER, REACTOR_VESSEL, CONDENSER)
                .build(null)
                .setRegistryName(new ResourceLocation(LibMisc.MODID, "machine_base")));
        event.getRegistry().register(TileEntityType.Builder.create(TileMachineDistiller::new, DISTILLER)
                .build(null)
                .setRegistryName(new ResourceLocation(LibMisc.MODID, "machine_distiller")));
    }
}
