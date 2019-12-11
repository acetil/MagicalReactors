package acetil.magicalreactors.common.fluid;

import acetil.magicalreactors.common.constants.Constants;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids {
    @ObjectHolder("ethanol_source")
    public static FlowingFluid STILL_ETHANOL;
    @ObjectHolder("ethanol_flowing")
    public static FlowingFluid FLOWING_ETHANOL;

    @SubscribeEvent
    public static void registerFluids (final RegistryEvent.Register<Fluid> event) {
        //event.getRegistry().register(new WaterFluid.Source().setRegistryName(new ResourceLocation(LibMisc.MODID, "ethanol_source")));
        //event.getRegistry().register(new WaterFluid.Flowing().setRegistryName(new ResourceLocation(LibMisc.MODID, "ethanol_flowing")));
        event.getRegistry().register(new FluidEthanol.Source("ethanol_source", 0xFFFFFFFF));
        event.getRegistry().register(new FluidEthanol.Flowing("ethanol_flowing", 0xFFFFFFFF));
        System.out.println("Registered fluids!");
    }
}
