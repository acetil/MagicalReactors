package acetil.magicalreactors.common.fluid;

import acetil.magicalreactors.common.constants.Constants;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MODID)
public class ModFluids {
    public static DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Constants.MODID);
    public static RegistryObject<Fluid> STILL_ETHANOL = FLUIDS.register("ethanol_source", () -> new FluidEthanol.Source("ethanol_source", 0xFFFFFFFF));
    public static RegistryObject<Fluid> FLOWING_ETHANOL = FLUIDS.register("ethanol_flowing", () -> new FluidEthanol.Flowing("ethanol_flowing", 0xFFFFFFFF));
}
