package acetil.magicalreactors.common;

import acetil.magicalreactors.common.capabilities.*;
import acetil.magicalreactors.common.containers.json.MachineContainerManager;
import acetil.magicalreactors.common.core.Config;
import acetil.magicalreactors.common.core.proxy.IProxy;
import acetil.magicalreactors.common.lib.LibMisc;
import acetil.magicalreactors.common.multiblock.MultiblockLoader;
import acetil.magicalreactors.common.network.PacketHandler;
import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import acetil.magicalreactors.common.worldgen.OreGen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nuclear.common.capabilities.*;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod(modid = NuclearMod.MODID, name = NuclearMod.MODNAME, version=NuclearMod.VERSION, useMetadata = true)
public class NuclearMod {
    public static final String MODID = LibMisc.MODID;
    public static final String MODNAME = LibMisc.MODNAME;
    public static final String VERSION = LibMisc.VERSION;

    @Mod.Instance(MODID)
    public static NuclearMod instance;

    @SidedProxy(serverSide = "nuclear.common.core.proxy.ServerProxy", clientSide = "nuclear.client.core.proxy.ClientProxy")
    public static IProxy proxy;


    public static Logger logger;

    public Configuration configGeneral;
    public Configuration configReactor;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
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
    }
}
