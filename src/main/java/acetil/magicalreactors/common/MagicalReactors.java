package acetil.magicalreactors.common;

import acetil.magicalreactors.client.core.proxy.ClientProxy;
import acetil.magicalreactors.client.gui.data.GuiDataManager;
import acetil.magicalreactors.client.gui.json.MachineGuiManager;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.*;
import acetil.magicalreactors.common.constants.ConfigConstants;
import acetil.magicalreactors.common.containers.json.MachineContainerManager;
import acetil.magicalreactors.common.core.proxy.IProxy;
import acetil.magicalreactors.common.core.proxy.ServerProxy;
import acetil.magicalreactors.common.datagen.DataGenerators;
import acetil.magicalreactors.common.event.MultiblockEventHandler;
import acetil.magicalreactors.common.fluid.ModFluids;
import acetil.magicalreactors.common.items.ModItems;
import acetil.magicalreactors.common.constants.Constants;
import acetil.magicalreactors.common.machines.MachineBlocks;
import acetil.magicalreactors.common.multiblock.MultiblockLoader;
import acetil.magicalreactors.common.network.PacketHandler;
import acetil.magicalreactors.common.reactor.ReactorFuelRegistry;
import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Constants.MODID)
public class MagicalReactors {
    public static final String MODID = Constants.MODID;
    public static final String MODNAME = Constants.MODNAME;
    public static final String VERSION = Constants.VERSION;

    public static IProxy proxy = DistExecutor.runForDist(()-> ClientProxy::new, ()-> ServerProxy::new);


    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon () {
            return new ItemStack(ModItems.ITEM_TEMP2.get());
        }
    };

    public MagicalReactors () {
        registerEventListeners();
        registerDeferredRegisters();
        MachineContainerManager.registerContainers();
        MachineBlocks.registerMachines();
        ConfigConstants.Server.bakeConfigs();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigConstants.SERVER_SPEC);

    }
    private void setup (final FMLCommonSetupEvent event) {
        registerCapabilities();
        ModItems.registerFuels();
        ModItems.registerCoolants();
        MachineRecipeManager.readRecipes("assets/magicalreactors/machine_recipes");
        PacketHandler.initMessages();
        MultiblockLoader.loadMultiblocks("assets/magicalreactors/multiblocks");
    }
    private void clientSetup (final FMLClientSetupEvent event) {
        GuiDataManager.addDefaultVariables();
        MachineGuiManager.registerGuis();
    }
    private void enqueueIMC (final InterModEnqueueEvent event) {

    }
    private void processIMC (final InterModProcessEvent event) {

    }
    private void registerCapabilities () {
        CapabilityMachine.register();
        CapabilityReactor.register();
        CapabilityReactorController.register();
        CapabilityReactorFuel.register();
        CapabilityReactorInterface.register();
    }
    private void registerEventListeners () {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(MultiblockEventHandler::blockBreakEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MultiblockEventHandler::blockPlaceEvent);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ReactorFuelRegistry::attachCapabilities);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::generateData);

    }
    private void registerDeferredRegisters () {
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModFluids.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MachineContainerManager.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    /*
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        initCapabilities();
        File directory = event.getModConfigurationDirectory();
        configGeneral = new Configuration(new File(directory.getPath(), MODID + ".cfg"));
        Config.readGeneralConfig(configGeneral);
        configReactor = new Configuration(new File(directory.getPath(), MODID + "_reactorItems.cfg"));
        Config.readReactorItemConfigs(configReactor);
        PacketHandler.initMessages();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.init(event);
        GameRegistry.registerWorldGenerator(new OreGen(), 0);
        MachineRecipeManager.readRecipes("assets/nuclearmod/machine_recipes");
        MachineContainerManager.readContainers("assets/nuclearmod/containers");
        MultiblockLoader.loadMultiblocks("assets/nuclearmod/multiblocks");
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        proxy.postInit(event);
        if (configGeneral.hasChanged()) {
            configGeneral.save();
        }
        if (configReactor.hasChanged()) {
            configReactor.save();
        }
    }
    public void initCapabilities () {
        CapabilityReactor.register();
        CapabilityReactorItem.register();
        CapabilityMachine.register();
        CapabilityReactorNew.register();
        CapabilityReactorController.register();
    }*/
}
