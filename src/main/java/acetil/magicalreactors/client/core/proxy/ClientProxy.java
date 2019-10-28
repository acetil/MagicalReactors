package acetil.magicalreactors.client.core.proxy;

import acetil.magicalreactors.common.core.proxy.IProxy;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import acetil.magicalreactors.client.gui.json.MachineGuiManager;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.items.ModItems;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MagicalReactors.instance, new GuiProxy());
        MachineGuiManager.readGuiJson("assets/nuclearmod/gui");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.initModels();
        ModBlocks.initModels();
    }
}
