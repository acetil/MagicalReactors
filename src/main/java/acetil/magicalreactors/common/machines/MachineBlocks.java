package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(LibMisc.MODID)
public class MachineBlocks {
    public static BlockMachine CENTRIFUGE = null;
    public static BlockMachine RECRYSTALISER = null;
    public static BlockMachine REACTOR_VESSEL = null;
    public static BlockMachine FERMENTER = null;
    public static BlockMachine ROD_FILLER = null;
    public static BlockMachine CONDENSER = null;
    public static BlockDistiller DISTILLER = null;
    public static void registerMachineBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockMachine("centrifuge", "centrifuge"));
        event.getRegistry().register(new BlockMachine("recrystaliser", "recrystaliser"));
        event.getRegistry().register(new BlockMachine("reactor_vessel", "reactor_vessel"));
        event.getRegistry().register(new BlockMachine("fermenter", "fermenter"));
        event.getRegistry().register(new BlockMachine("rod_filler", "rod_filler"));
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
        ModBlocks.registerItemBlock(event, ROD_FILLER);
        ModBlocks.registerItemBlock(event, CONDENSER);
        ModBlocks.registerItemBlock(event, DISTILLER);
    }

}
