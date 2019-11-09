package acetil.magicalreactors.common.fluid;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Rarity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
public abstract class FluidEthanol extends ForgeFlowingFluid {
    private boolean isSource;
    public FluidEthanol (String name, boolean isSource, int colour, String stillTex, String flowingTex) {
        // TODO: update to work with nbt
        super(new Properties(() -> ModFluids.STILL_ETHANOL, () -> ModFluids.FLOWING_ETHANOL,
                FluidAttributes.builder(new ResourceLocation(LibMisc.MODID, stillTex),
                        new ResourceLocation(LibMisc.MODID, flowingTex))
                        .density(789)
                        .rarity(Rarity.COMMON)
                        .viscosity(1230)
                        .color(colour)
                        .translationKey(name))
                        .block(() -> ModBlocks.ETHANOL_BLOCK));
        setRegistryName(new ResourceLocation(LibMisc.MODID, name));
        this.isSource = isSource;
    }
    public FluidEthanol (String name, boolean isSource, int colour) {
        this(name, isSource, colour, "fluids/" + name + "_still", "fluids/" + name + "_flowing");
    }

    public static class Source extends FluidEthanol {

        public Source(String name, int colour, String stillTex, String flowingTex) {
            super(name, true, colour, stillTex, flowingTex);
        }
        public Source (String name, int colour) {
            super(name, true, colour);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return true;
        }

        @Override
        public int getLevel(IFluidState p_207192_1_) {
            return 8;
        }
    }
    public static class Flowing extends FluidEthanol {

        public Flowing(String name, int colour, String stillTex, String flowingTex) {
            super(name, false, colour, stillTex, flowingTex);
        }
        public Flowing (String name, int colour) {
            super(name, false, colour);
        }
        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
        }

        @Override
        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }
    }
}
