package acetil.magicalreactors.common.fluid;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.constants.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
public abstract class FluidEthanol extends ForgeFlowingFluid {
    private boolean isSource;
    private ResourceLocation stillTex;
    private ResourceLocation flowingTex;
    public FluidEthanol (String name, boolean isSource, int colour, String stillTex, String flowingTex) {
        // TODO: update to work with nbt
        super(new Properties(ModFluids.STILL_ETHANOL, ModFluids.FLOWING_ETHANOL,
                FluidAttributes.builder(new ResourceLocation(Constants.MODID, stillTex),
                        new ResourceLocation(Constants.MODID, flowingTex))
                        .density(789)
                        .rarity(Rarity.COMMON)
                        .viscosity(1230)
                        .color(colour)
                        .translationKey(name))
                        .block(() -> (LiquidBlock) ModBlocks.ETHANOL_BLOCK.get()));
        this.stillTex = new ResourceLocation(Constants.MODID, stillTex);
        this.flowingTex = new ResourceLocation(Constants.MODID, flowingTex);
        this.isSource = isSource;
    }
    public FluidEthanol (String name, boolean isSource, int colour) {
        this(name, isSource, colour, "fluids/" + name + "_still", "fluids/" + name + "_flowing");
    }

    public ResourceLocation getStillTexture () {
        return stillTex;
    }

    public ResourceLocation getFlowingTexture () {
        return flowingTex;
    }

    public static class Source extends FluidEthanol {

        public Source(String name, int colour, String stillTex, String flowingTex) {
            super(name, true, colour, stillTex, flowingTex);
        }
        public Source (String name, int colour) {
            super(name, true, colour);
        }

        @Override
        public boolean isSource (FluidState pState) {
            return true;
        }

        @Override
        public int getAmount (FluidState pState) {
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
        protected void createFluidStateDefinition (StateDefinition.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        @Override
        public boolean isSource (FluidState pState) {
            return false;
        }

        @Override
        public int getAmount (FluidState pState) {
            return pState.getValue(LEVEL);
        }
    }
}
