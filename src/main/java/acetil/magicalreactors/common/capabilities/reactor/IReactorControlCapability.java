package acetil.magicalreactors.common.capabilities.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;

public interface IReactorControlCapability {
    void setPosition (LevelReader worldIn, BlockPos pos);
    BlockPos getPosition ();
    void setIsPowered (boolean isPowered);
    void update ();
    void setReactorHandler (IReactorHandlerNew reactorHandler);
    boolean isOn ();
    void checkMultiblock ();
    void debugPrint (Player player);
    void readNBT (CompoundTag nbt);
    CompoundTag writeNBT ();
}
