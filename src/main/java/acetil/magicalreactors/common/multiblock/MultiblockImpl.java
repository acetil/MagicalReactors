package acetil.magicalreactors.common.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import acetil.magicalreactors.common.MagicalReactors;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Predicate;

public class MultiblockImpl implements IMultiblock{
    private Map<Character, Predicate<IBlockState>> keyMap = new HashMap<>();
    private String[][] multiblock;
    private boolean allowsFilledAirBlocks;
    boolean complete = true;
    private String name;
    private List<BlockOffset> offsets;
    private int[] offsetCoord = new int[3];
    private static char OFFSET_CHAR = '0';
    private String type;
    public MultiblockImpl (MultiblockLoader.MultiblockJson json) {
        allowsFilledAirBlocks = json.allowsFilledAir;
        name = json.name;
        multiblock = json.multiblock;
        type = json.type;
        generateKeyMap(json.keyMap);
        handleParity();
        validateOffsetPoint();
        if (!complete) {
            MagicalReactors.LOGGER.log(Level.WARN, "Multiblock \"" + name + "\" has failed loading!");
        } else {
            getBlockOffsets();
        }
    }
    @Override
    public IMultiblockValidator getMultiblockValidator(World worldIn, BlockPos pos) {
        return new MultiblockValidatorImpl(offsets, worldIn, pos);
    }
    @Override
    public boolean allowsFilledAirBlocks() {
        return allowsFilledAirBlocks;
    }

    @Override
    public String getType() {
        return type;
    }

    private void generateKeyMap (Map<String, String> stringMap) {
        keyMap.put(' ', getPredicate("minecraft:air"));
        for (String key : stringMap.keySet()) {
            if (key.length() > 1) {
                MagicalReactors.LOGGER.log(Level.WARN, "Multiblock \"" + name + "\" has an invalid key \"" + key + "\"!");
                complete = false;
                continue;
            }
            if (keyMap.containsKey(key.charAt(0))) {
                MagicalReactors.LOGGER.log(Level.WARN, "Multiblock \"" + name + "\" has duplicate key '" + key + "'");
                complete = false;
                continue;
            }
            keyMap.put(key.charAt(0), getPredicate(stringMap.get(key)));
        }
        if (!keyMap.containsKey(OFFSET_CHAR)) {
            keyMap.put(OFFSET_CHAR, getPredicate("minecraft:air"));
        }
    }
    private Predicate<IBlockState> getPredicate (String key) {
        // TODO: update to more than just blocks
        Block b = Block.getBlockFromName(key);
        System.out.println(String.format("Key: %s, name: %s", key, b.getRegistryName()));
        if (b == Blocks.AIR && allowsFilledAirBlocks) {
            return (IBlockState state) -> true;
        }
        return (IBlockState state) -> state.getBlock() == b;
    }
    private void handleParity () {
        String[] addedLayer = {" "};
        for (int i = 0; i < multiblock.length; i++) {
            for (int j = 0; j < multiblock[i].length; j++) {
                if (multiblock[i][j].length() % 2 == 0) {
                    multiblock[i][j] += " ";
                }
            }
            if (multiblock[i].length % 2 == 0) {
                multiblock[i] = appendArray(multiblock[i], " ");
            }
        }
        if (multiblock.length % 2 == 0) {
            multiblock = appendArray(multiblock, addedLayer);
        }
    }
    private void validateOffsetPoint () {
        boolean hasOffsetPoint = false;
        for (String[] layer : multiblock) {
            for (String slice : layer) {
                for (int i = 0; i < slice.length(); i++) {
                    if (slice.charAt(i) == OFFSET_CHAR) {
                        if (hasOffsetPoint) {
                            MagicalReactors.LOGGER.log(Level.WARN, "Multiblock \"" + name + "\" has too many offset points!");
                            complete = false;
                            break;
                        }
                        hasOffsetPoint = true;
                    }
                }
            }
        }
        if (!hasOffsetPoint) {
            MagicalReactors.LOGGER.log(Level.WARN, "Multiblock \"" + name + "\" has no offset point!");
            complete = false;
        }
    }
    private void getBlockOffsets () {
        getOffsetCoord();
        offsets = new ArrayList<>();
        int layerNum = 0;
        for (String[] layer : multiblock) {
            int sliceNum = 0;
            for (String slice : layer) {
                for (int i = 0; i < slice.length(); i++) {
                    offsets.add(new BlockOffset(keyMap.get(slice.charAt(i)), i - slice.length() / 2 - offsetCoord[0],
                            layerNum - multiblock.length / 2 - offsetCoord[1] - 1, sliceNum - layer.length / 2 - offsetCoord[2]));
                }
                sliceNum++;
            }
            layerNum++;
        }
    }
    private void getOffsetCoord () {
        // TODO: combine with validateOffsetPoint maybe / refactor
        int[] coord = new int[3];
        int layerNum = 0;
        boolean done = false;
        for (String[] layer : multiblock) {
            int sliceNum = 0;
            for (String slice : layer) {
                for (int i = 0; i < slice.length(); i++) {
                    if (slice.charAt(i) == OFFSET_CHAR) {
                        coord[0] = i - slice.length() / 2; // for length 2n + 1, middle is position n because arrays start at 0
                        coord[2] = sliceNum - layer.length / 2;
                        coord[1] = layerNum - multiblock.length + 1;
                        done = true;
                        break;
                    }
                }
                if (done) {
                    break;
                }
                sliceNum++;
            }
            if (done) {
                break;
            }
            layerNum++;
        }
        offsetCoord = coord;
    }
    @SuppressWarnings("unchecked")
    private <T> T[] appendArray (T[] array, T append) {
        List<T> newList = new LinkedList<>(Arrays.asList(array));
        newList.add(append);
        return (T[]) newList.toArray();
    }
    public static class BlockOffset {
        Predicate<IBlockState> predicate;
        int xOffset;
        int yOffset;
        int zOffset;
        public BlockOffset (Predicate<IBlockState> predicate, int xOffset, int yOffset, int zOffset) {
            this.predicate = predicate;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
        }
        public BlockOffset getRotatedOffset () {
            return new BlockOffset(predicate, zOffset * -1, yOffset, xOffset); // (cis x) * i
        }
        public BlockPos getBlockPos (BlockPos offsetPoint) {
            return new BlockPos(offsetPoint.getX() + xOffset, offsetPoint.getY() + yOffset, offsetPoint.getZ() + zOffset);
        }
        public Predicate<IBlockState> getPredicate () {
            return predicate;
        }
    }

}
