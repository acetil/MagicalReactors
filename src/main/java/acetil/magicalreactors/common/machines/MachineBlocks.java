package acetil.magicalreactors.common.machines;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibGui;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MODID)
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
                .setRegistryName(new ResourceLocation(Constants.MODID, "machine_base")));
        event.getRegistry().register(TileEntityType.Builder.create(TileMachineDistiller::new, DISTILLER)
                .build(null)
                .setRegistryName(new ResourceLocation(Constants.MODID, "machine_distiller")));
    }
}
