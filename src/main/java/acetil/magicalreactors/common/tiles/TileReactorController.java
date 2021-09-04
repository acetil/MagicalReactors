package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.block.ModBlocks;
import acetil.magicalreactors.common.capabilities.CapabilityReactorController;
import acetil.magicalreactors.common.capabilities.reactor.IReactorControlCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import acetil.magicalreactors.common.capabilities.reactor.ReactorControlHandler;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TileReactorController extends BlockEntity {
    private ReactorControlHandler reactorhandler;
    private LazyOptional<IReactorControlCapability> reactorOptional;
    private boolean first = true;
    private BlockPos pos;
    public TileReactorController (BlockPos pos, BlockState state) {
        super(ModBlocks.REACTOR_CONTROLLER_TILE.get(), pos, state);
        reactorhandler = new ReactorControlHandler();
        reactorOptional = LazyOptional.of(() -> reactorhandler);
        this.pos = pos;
    }
    @Override
    public void tick () {
        if (first) {
            // no forge onload >:(
            reactorhandler.onCreation();
            first = false;
            this.setChanged();
        }
        reactorhandler.update();
    }
    public void setPosition (Level worldIn, BlockPos pos) {
        reactorhandler.setPosition(worldIn, pos);
    }
    public void setPosition () {
        reactorhandler.setPosition(level, pos);
        System.out.println("Setting position!");
    }

    public void setPos (BlockPos pos) {
        this.pos = pos;
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
    public void setRemoved () {
        reactorhandler.onDestruction();
        super.setRemoved();
    }

    /*@Override
    public void read (CompoundNBT compound) {
        super.read(compound);
        CompoundNBT handlerCompound = compound.getCompound("handler");
        reactorhandler.readNBT(handlerCompound);
    }

    @Override
    public CompoundNBT write (CompoundNBT compound) {
        compound.put("handler", reactorhandler.writeNBT());
        return super.write(compound);
    }*/

    @Override
    public CompoundTag serializeNBT () {
        var nbt =  super.serializeNBT();
        nbt.put("handler", reactorhandler.writeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT (CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("handler")) {
            reactorhandler.readNBT(nbt.getCompound("handler"));
        }
    }
}
