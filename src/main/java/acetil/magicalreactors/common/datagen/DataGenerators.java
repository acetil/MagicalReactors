package acetil.magicalreactors.common.datagen;

import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.block.reactor.BlockRuneBase;
import acetil.magicalreactors.common.constants.Constants;
import acetil.magicalreactors.common.machines.BlockMachine;
import acetil.magicalreactors.common.machines.MachineBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.Level;

public class DataGenerators {
    public static void generateData (GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockStates(event.getGenerator(), event.getExistingFileHelper()));
    }
    public static class BlockStates extends BlockStateProvider {
        private static final String MACHINE_MODEL_PREFIX = "block";
        private static final String RUNE_TEXTURE_PREFIX = "block";
        private static final String RUNE_BASIC_TEXTURE = "wip-rune-basic-3";
        private static final String RUNE_BASIC_MULTI_TEXTURE = "wip-rune-basic";
        private static final String MULTIBLOCK_SUFFIX = "_multi";
        public BlockStates (DataGenerator gen,  ExistingFileHelper exFileHelper) {
            super(gen, Constants.MODID, exFileHelper);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void registerStatesAndModels () {
            // machines
            registerMachine(MachineBlocks.CENTRIFUGE.get());
            registerMachine(MachineBlocks.CONDENSER.get());
            registerMachine(MachineBlocks.DISSOLVER.get());
            registerMachine(MachineBlocks.FERMENTER.get());
            registerMachine(MachineBlocks.REACTOR_VESSEL.get());
            //registerMachine(MachineBlocks.RECRYSTALISER.get());
            // TODO: add distiller

            // runes
            registerRuneAll(ModBlocks.RUNE_BASIC.get(),
                    getModelLocation(ModBlocks.RUNE_BASIC.get(), RUNE_TEXTURE_PREFIX, RUNE_BASIC_TEXTURE),
                    getModelLocation(ModBlocks.RUNE_BASIC.get(), RUNE_TEXTURE_PREFIX, RUNE_BASIC_MULTI_TEXTURE));

            registerRuneTop(ModBlocks.RUNE_LOCUS.get(),
                    getModelLocation(ModBlocks.RUNE_LOCUS.get(), RUNE_TEXTURE_PREFIX, "wip-rune-locus"));
            registerRuneTop(ModBlocks.RUNE_STABILISATION.get(),
                    getModelLocation(ModBlocks.RUNE_STABILISATION.get(), RUNE_TEXTURE_PREFIX, "wip-rune-stab"));
            registerRuneTop(ModBlocks.RUNE_TRANSFER.get(),
                    getModelLocation(ModBlocks.RUNE_TRANSFER.get(), RUNE_TEXTURE_PREFIX, "wip-rune-transfer"));
            // TODO: harmonic

            // reactor stuff
            registerRuneAll(ModBlocks.BYPRODUCT_INTERFACE.get(),
                    getModelLocation(ModBlocks.BYPRODUCT_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-rune-byproduct"),
                    getModelLocation(ModBlocks.BYPRODUCT_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-rune-byproduct"));
            registerRuneTop(ModBlocks.COOLING_INTERFACE.get(),
                    getModelLocation(ModBlocks.COOLING_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-coolant-interface"));
            registerRuneAll(ModBlocks.ENERGY_INTERFACE.get(),
                    getModelLocation(ModBlocks.ENERGY_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-energy-interface"),
                    getModelLocation(ModBlocks.ENERGY_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-energy-interface"));
            registerRuneTop(ModBlocks.FUEL_INTERFACE.get(),
                    getModelLocation(ModBlocks.FUEL_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-fuel-loader"));
            registerRuneAll(ModBlocks.REACTOR_CONTROLLER.get(),
                    getModelLocation(ModBlocks.FUEL_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-reactor-controller"),
                    getModelLocation(ModBlocks.FUEL_INTERFACE.get(), RUNE_TEXTURE_PREFIX, "wip-reactor-controller"));
            // TODO: redstone

            MagicalReactors.LOGGER.log(Level.INFO, "Generated blockstates and models successfully!");
        }
        protected void registerMachine (BlockMachine machineBlock) {
            simpleBlock(machineBlock,
                    new ModelFile.ExistingModelFile(getModelLocation(machineBlock, MACHINE_MODEL_PREFIX),
                    existingFileHelper));
        }
        protected ResourceLocation getModelLocation (Block b, String prefix) {
            return getModelLocation(b, prefix, b.getRegistryName().getPath());
        }
        protected ResourceLocation getModelLocation (Block b, String prefix, String location) {
            return getModelLocation(b.getRegistryName(), prefix, location);
        }
        protected ResourceLocation getModelLocation (ResourceLocation blockLocation, String prefix, String location) {
            if (!prefix.equals("")) {
                return new ResourceLocation(blockLocation.getNamespace(), prefix + "/" + location);
            }
            return new ResourceLocation(blockLocation.getNamespace(), location);
        }
        protected ModelFile getSpecialRuneTopModel (Block rune, boolean isMulti, ResourceLocation topTex) {
            ResourceLocation runeLoc = rune.getRegistryName();
            if (isMulti) {
                return cubeTop(runeLoc.toString() + MULTIBLOCK_SUFFIX, getModelLocation(rune, RUNE_TEXTURE_PREFIX, RUNE_BASIC_MULTI_TEXTURE),
                        topTex);
            }
            else {
                return cubeTop(runeLoc.toString(), getModelLocation(rune, RUNE_TEXTURE_PREFIX, RUNE_BASIC_TEXTURE),
                        topTex);
            }
        }
        protected void registerRuneTop (Block rune, ResourceLocation topTex) {
            registerRuneTop(rune, topTex, topTex);
        }
        protected void registerRuneTop (Block rune, ResourceLocation topTex, ResourceLocation topTexMulti) {
            registerRune(rune, getSpecialRuneTopModel(rune, false, topTex), getSpecialRuneTopModel(rune, true, topTexMulti));
        }
        protected void registerRuneAll (Block rune, ResourceLocation allTex, ResourceLocation allTexMulti) {
            registerRune(rune, cubeAll(rune.getRegistryName().toString(), allTex),
                    cubeAll(rune.getRegistryName().toString() + MULTIBLOCK_SUFFIX, allTexMulti));
        }
        protected void registerRune (Block rune, ModelFile model, ModelFile modelMulti) {
            getVariantBuilder(rune).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.get(BlockRuneBase.MULTIBLOCK_STATE) ? modelMulti : model).build());
        }
    }

}
