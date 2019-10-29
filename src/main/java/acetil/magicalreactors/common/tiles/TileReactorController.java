package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.CapabilityReactorController;
import acetil.magicalreactors.common.capabilities.reactor.IReactorControlCapability;
import acetil.magicalreactors.common.lib.LibMisc;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import acetil.magicalreactors.common.capabilities.reactor.ReactorControlHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class TileReactorController extends TileEntity implements ITickableTileEntity {
    private ReactorControlHandler reactorhandler;
    private LazyOptional<IReactorControlCapability> reactorOptional;
    public TileReactorController () {
        super(ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(LibMisc.MODID, "reactor_controller_entity")));
        reactorhandler = new ReactorControlHandler();
        reactorOptional = LazyOptional.of(() -> reactorhandler);
    }
    @Override
    public void tick () {

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
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityReactorController.REACTOR_CONTROLLER) {
            return reactorOptional.cast();
        }
        return super.getCapability(capability, side);
    }
    public void updateMultiblock () {
        reactorhandler.checkMultiblock();
    }
}
