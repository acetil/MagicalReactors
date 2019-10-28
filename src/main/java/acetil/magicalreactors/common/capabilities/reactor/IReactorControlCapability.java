package acetil.magicalreactors.common.capabilities.reactor;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IReactorControlCapability {
    void setPosition (World worldIn, BlockPos pos);
    BlockPos getPosition ();
    void setIsPowered (boolean isPowered);
    void update ();
    void setReactorHandler (IReactorHandlerNew reactorHandler);
    boolean isOn ();
    void checkMultiblock ();
}
