package acetil.magicalreactors.common.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

/*
    Checks whether multiblock placed in world is valid or not
*/
public interface IMultiblockValidator {
    boolean isValid();
    void updateAll();

    int getNumInvalidBlocks ();
    List<BlockPos> getInvalidBlocks();

    List<BlockPos> getPositionsOfBlock(Block b);
    List<BlockPos> getPositionsOfType (Class <?> blockType);
    List<BlockPos> getPositionsWithCapability (Capability<?> capability, Direction side);
    boolean contains (BlockPos pos);
    void update (BlockPos pos, BlockState newState);
}