package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
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
    private boolean first = true;
    public TileReactorController () {
        super(ModBlocks.REACTOR_CONTROLLER_TILE_ENTITY);
        reactorhandler = new ReactorControlHandler();
        reactorOptional = LazyOptional.of(() -> reactorhandler);
    }
    @Override
    public void tick () {
        if (first) {
            // no forge onload >:(
            reactorhandler.onCreation();
            first = false;
        }
        reactorhandler.update();
    }
    public void setPosition (World worldIn, BlockPos pos) {
        reactorhandler.setPosition(worldIn, pos);
    }
    public void setPosition () {
        reactorhandler.setPosition(world, pos);
        System.out.println("Setting position!");
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
    @Override
    public void remove () {
        reactorhandler.onDestruction();
        super.remove();
    }
}
