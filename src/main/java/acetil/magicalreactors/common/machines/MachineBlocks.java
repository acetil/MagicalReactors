package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.lib.LibMisc;

public class MachineBlocks {
    public static BlockMachine CENTRIFUGE = new BlockMachine("centrifuge", "centrifuge");
    public static BlockMachine RECRYSTALISER = new BlockMachine("recrystaliser", "recrystaliser");
    public static BlockMachine REACTOR_VESSEL = new BlockMachine("reactor_vessel", "reactor_vessel");
    public static BlockMachine FERMENTER = new BlockMachine("fermenter", "fermenter");
    public static BlockMachine ROD_FILLER = new BlockMachine("rod_filler", "rod_filler");
    public static BlockMachine CONDENSER = new BlockMachine("condenser", "condenser");
    public static BlockDistiller DISTILLER = new BlockDistiller("distiller", "distiller", 2);
    public static void registerMachineBlocks (RegistryEvent.Register<Block> event) {
        event.getRegistry().register(CENTRIFUGE);
        event.getRegistry().register(RECRYSTALISER);
        event.getRegistry().register(REACTOR_VESSEL);
        event.getRegistry().register(FERMENTER);
        event.getRegistry().register(ROD_FILLER);
        event.getRegistry().register(DISTILLER);

        GameRegistry.registerTileEntity(TileMachineBase.class, new ResourceLocation(LibMisc.MODID + ":machine_entity"));
        GameRegistry.registerTileEntity(TileMachineDistiller.class, new ResourceLocation(LibMisc.MODID + ":distiller_entity"));

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
    @SideOnly(Side.CLIENT)
    public static void initMachineModels () {
        CENTRIFUGE.initModel();
        RECRYSTALISER.initModel();
        REACTOR_VESSEL.initModel();
        FERMENTER.initModel();
        ROD_FILLER.initModel();
        CONDENSER.initModel();
        DISTILLER.initModel();
    }

}
