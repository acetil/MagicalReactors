package acetil.magicalreactors.common.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import acetil.magicalreactors.common.MagicalReactors;
import acetil.magicalreactors.common.block.ModBlocks;

import java.util.Random;
import static acetil.magicalreactors.common.lib.LibOre.*;

public class OreGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.getDimension()) {
            case -1:
            case 1:
            break;
            default: {
                runGenerator(ModBlocks.TEMP_ORE1.getDefaultState(), TEMP_ORE1_AMOUNT, TEMP_ORE1_CHANCES_SPAWN,
                        TEMP_ORE1_MIN_HEIGHT, TEMP_ORE1_MAX_HEIGHT, BlockMatcher.forBlock(Blocks.STONE), world,
                        random, chunkX, chunkZ);
                runGenerator(ModBlocks.TEMP_ORE2.getDefaultState(), TEMP_ORE2_AMOUNT, TEMP_ORE2_CHANCES_SPAWN,
                        TEMP_ORE2_MIN_HEIGHT, TEMP_ORE2_MAX_HEIGHT, BlockMatcher.forBlock(Blocks.STONE), world,
                        random, chunkX, chunkZ);
            }
            break;
        }
    }
    public void runGenerator (IBlockState blockToGen, int blockAmount, int chancesToSpawn, int minHeight, int maxHeight,
                              Predicate<IBlockState> blocksToReplace, World world, Random rand, int chunkX, int chunkZ) {
        if (minHeight < 0 || minHeight > 256 || maxHeight < 0 || maxHeight > 256) {
            MagicalReactors.LOGGER.error("Invalid height arguments in worldgen for block " + blockToGen.getBlock().getUnlocalizedName());
            return;
        }
        WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, blocksToReplace);
        int heightDiff = maxHeight - minHeight + 1;
        for (int i = 0; i < chancesToSpawn; i++) {
            //TODO update see https://www.reddit.com/r/feedthebeast/comments/5x0twz/investigating_extreme_worldgen_lag/
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightDiff);
            int z = chunkZ * 16 + rand.nextInt(16);

            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
