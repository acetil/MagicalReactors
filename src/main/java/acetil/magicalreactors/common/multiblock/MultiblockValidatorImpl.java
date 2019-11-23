package acetil.magicalreactors.common.multiblock;

import acetil.magicalreactors.common.MagicalReactors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MultiblockValidatorImpl implements IMultiblockValidator {
    private List<LockedValidator> validators;
    private World world;
    private BlockPos offsetPos;
    private boolean valid;
    private Orientation orientation = Orientation.NONE;
    private Set<BlockPos> positions;
    public MultiblockValidatorImpl (List<MultiblockImpl.BlockOffset> offsets, World worldIn, BlockPos pos) {
        this.world = worldIn;
        this.offsetPos = pos;
        this.valid = false;
        System.out.println(String.format("Offset: (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
        createValidators(offsets);
        updatePositions();
    }
    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void updateAll() {
        valid = false;
        for (LockedValidator l : validators) {
            System.out.println("Checked orientation:");
            l.orientation.printOrientation();
            l.updateAll();
            valid |= l.valid;
            if (valid) {
                orientation = l.orientation;
                //System.out.println("Valid orientation:");
                //l.orientation.printOrientation();
                break;
            }
        }
        if (!valid) {
            orientation = validators.stream()
                                    .min(Comparator.comparingInt(LockedValidator::getNumIncorrectBlocks))
                                    .get().orientation;
        }
    }

    @Override
    public int getNumInvalidBlocks() {
        return getOrientation().getNumIncorrectBlocks();
    }

    @Override
    public List<BlockPos> getInvalidBlocks() {
        return getOrientation().incorrectBlocks;
    }

    @Override
    public List<BlockPos> getPositionsOfBlock(Block b) {
        return getOrientation().predicates
                               .stream()
                               .filter((BlockPredicate pred) -> world.getBlockState(pred.pos).getBlock() == b)
                               .map(BlockPredicate::getPos)
                               .collect(Collectors.toList());
    }

    @Override
    public List<BlockPos> getPositionsOfType(Class<?> blockType) {
        return getOrientation().predicates
                               .stream()
                               .filter((BlockPredicate pred) -> blockType.isInstance(world.getBlockState(pred.pos).getBlock()))
                               .map(BlockPredicate::getPos)
                               .collect(Collectors.toList());
    }
    @Override
    public List<BlockPos> getPositionsWithCapability(Capability<?> capability, Direction side) {
        return getOrientation().predicates
                               .stream()
                               .filter((BlockPredicate pred) -> world.getTileEntity(pred.pos) != null &&
                                       world.getTileEntity(pred.pos).getCapability(capability, side).isPresent())
                               .map(BlockPredicate::getPos)
                               .collect(Collectors.toList());
    }

    @Override
    public boolean contains(BlockPos pos) {
        return positions.contains(pos);
    }

    @Override
    public void update(BlockPos pos, BlockState newState) {
        if (!contains(pos)) {
            return;
        }
        valid = false;
        for (LockedValidator l : validators) {
            l.update(pos, newState);
            valid |= l.valid;
        }
    }

    private LockedValidator getOrientation () {
        // TODO: consider rename
        for (LockedValidator l : validators) {
            if (l.orientation == orientation) {
                //System.out.println("Orientation gotten:");
                orientation.printOrientation();
                return l;
            }
        }
        return validators.get(0);
    }
    private void createValidators (List<MultiblockImpl.BlockOffset> offsets) {
        validators = new ArrayList<>();
        List<MultiblockImpl.BlockOffset> offsetTemp = new ArrayList<>(offsets);
        Orientation orientation = Orientation.ZERO;
        for (int i = 0; i < 4; i++) {
            validators.add(new LockedValidator(offsetTemp, world, offsetPos, orientation));
            //orientation.printOrientation();
            offsetTemp = rotateOffsets(offsetTemp);
            orientation = orientation.getNextOrientation();
        }
    }
    private List<MultiblockImpl.BlockOffset> rotateOffsets (List<MultiblockImpl.BlockOffset> offsets) {
        return offsets.stream().map(MultiblockImpl.BlockOffset::getRotatedOffset).collect(Collectors.toList());
    }

    private void updatePositions () {
        positions = new HashSet<>();
        for (LockedValidator validator : validators) {
            positions.addAll(validator.predicates.stream().map(BlockPredicate::getPos).collect(Collectors.toList()));
        }
    }

    private static class LockedValidator {
        List<BlockPredicate> predicates;
        World world;
        BlockPos offsetPos;
        Orientation orientation;
        List<BlockPos> incorrectBlocks;
        int numIncorrectBlocks;
        boolean valid;
        LockedValidator(List<MultiblockImpl.BlockOffset> offsets, World worldIn, BlockPos pos, Orientation orientation) {
            predicates = offsets.stream()
                                .map((MultiblockImpl.BlockOffset offset) ->
                                        new BlockPredicate(worldIn, offset.getBlockPos(pos), offset.getPredicate()))
                                .collect(Collectors.toList());
            world = worldIn;
            offsetPos = pos;
            this.orientation = orientation;
            incorrectBlocks = new LinkedList<>();
            valid = false;
        }
        public void updateAll () {
            incorrectBlocks.clear();
            for (BlockPredicate predicate : predicates) {
                if (!predicate.test()) {
                    incorrectBlocks.add(predicate.pos);
                }
            }
            numIncorrectBlocks = incorrectBlocks.size();
            valid = numIncorrectBlocks == 0;
        }
        public int getNumIncorrectBlocks () {
            return numIncorrectBlocks;
        }
        void update (BlockPos pos, BlockState state) {
            MagicalReactors.LOGGER.debug("Blockpos: ({},{},{})", pos.getX(), pos.getY(), pos.getZ());
            if (incorrectBlocks.contains(pos)) {
                incorrectBlocks.remove(pos);
                numIncorrectBlocks--;
            }
            BlockPredicate pred = null;
            for (BlockPredicate p : predicates) {
                if (pos.equals(p.pos)) {
                    pred = p;
                    break;
                }
            }
            if (pred == null) {
                MagicalReactors.LOGGER.debug("Null pos predicate!");
                return;
            }
            if (!pred.statePredicate.test(state)) {
                incorrectBlocks.add(pos);
                numIncorrectBlocks++;
                MagicalReactors.LOGGER.debug("Incorrect block! Num incorrect blocks = {}", numIncorrectBlocks);
            }
            valid = numIncorrectBlocks == 0;
            System.out.println("Valid: " + valid);
        }
    }
    public static class BlockPredicate {
        World world;
        BlockPos pos;
        Predicate<BlockState> statePredicate;
        BlockPredicate(World worldIn, BlockPos pos, Predicate<BlockState> statePredicate) {
            this.world = worldIn;
            this.pos = pos;
            this.statePredicate = statePredicate;
        }
        boolean test () {
            return statePredicate.test(world.getBlockState(pos));
        }
        BlockPos getPos () {
            return pos;
        }
    }
    private enum Orientation {
        NONE (-1, "none"), ZERO (0, "zero"), ONE (1, "one"), TWO (2, "two"), THREE (3, "three");
        protected int num;
        protected String name;
        Orientation (int num, String name) {
            this.num = num;
            this.name = name;
        }
        public Orientation getNextOrientation () {
            switch (num) {
                case 0:
                    return ONE;
                case 1:
                    return TWO;
                case 2:
                    return THREE;
                case 3:
                case -1:
                default:
                    return ZERO;
            }
        }
        public void printOrientation () {
            System.out.println("Orientation: " + name);
        }
    }
}
