package acetil.magicalreactors.common.core.proxy;

import acetil.magicalreactors.client.core.proxy.GuiProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import acetil.magicalreactors.common.NuclearMod;

@Mod.EventBusSubscriber
public class ServerProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(NuclearMod.instance, new GuiProxy());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
