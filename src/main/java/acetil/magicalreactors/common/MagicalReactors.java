package acetil.magicalreactors.common;

import acetil.magicalreactors.client.core.proxy.ClientProxy;
import acetil.magicalreactors.common.capabilities.*;
import acetil.magicalreactors.common.containers.json.MachineContainerManager;
import acetil.magicalreactors.common.core.Config;
import acetil.magicalreactors.common.core.proxy.IProxy;
import acetil.magicalreactors.common.core.proxy.ServerProxy;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.multiblock.MultiblockLoader;
import acetil.magicalreactors.common.network.PacketHandler;
import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import acetil.magicalreactors.common.worldgen.OreGen;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod("magicalreactors")
public class MagicalReactors {
    public static final String MODID = LibMisc.MODID;
    public static final String MODNAME = LibMisc.MODNAME;
    public static final String VERSION = LibMisc.VERSION;

    public static IProxy proxy = DistExecutor.runForDist(()-> ClientProxy::new, ()-> ServerProxy::new);


    public static final Logger LOGGER = LogManager.getLogger();

    public MagicalReactors () {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
    }
    private void setup (final FMLCommonSetupEvent event) {

    }
    private void clientSetup (final FMLClientSetupEvent event) {

    }
    private void enqueueIMC (final InterModEnqueueEvent event) {

    }
    private void processIMC (final InterModProcessEvent event) {

    }/*
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
