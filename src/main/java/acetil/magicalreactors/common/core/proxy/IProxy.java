package acetil.magicalreactors.common.core.proxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public interface IProxy {
    public void preInit (FMLPreInitializationEvent event);
    public void init (FMLInitializationEvent event);
    public void postInit (FMLPostInitializationEvent event);
}
