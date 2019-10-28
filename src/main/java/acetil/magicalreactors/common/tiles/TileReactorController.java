package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactorController;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import acetil.magicalreactors.common.capabilities.reactor.ReactorControlHandler;

import javax.annotation.Nullable;

public class TileReactorController extends TileEntity implements ITickable {
    private ReactorControlHandler reactorhandler;

    public TileReactorController () {
        reactorhandler = new ReactorControlHandler();
    }
    @Override
    public void update() {

    }
    public void setPosition (World worldIn, BlockPos pos) {
        reactorhandler.setPosition(worldIn, pos);
    }
    public void setPosition () {
        reactorhandler.setPosition(world, pos);
    }
    @Override
    public void setPos (BlockPos pos) {
        super.setPos(pos);
        setPosition();
    }
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityReactorController.REACTOR_CONTROLLER) {
            return CapabilityReactorController.REACTOR_CONTROLLER.cast(reactorhandler);
        }
        return super.getCapability(capability, side);
    }
    public void updateMultiblock () {
        reactorhandler.checkMultiblock();
    }
}
