package acetil.magicalreactors.common.tiles;

import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import acetil.magicalreactors.common.lib.LibReactor;

import javax.annotation.Nullable;
import java.util.*;

public class TileReactor extends TileEntity implements ITickable{
    public int columns = 9;
    public int numChambers = 0;
    public boolean active = true;
    public int ticksSinceUpdate = 0;
    protected static int TICKS_PER_UPDATE = 20;
    public static final int COLUMN_LENGTH = 6;
    protected static final double REACH_DIST = 64D;
    private ItemStackHandler itemStackHandler;
    private EnergyHandler energyHandler;
    private ReactorHandler reactorHandler;
    protected List<Item> reactorItems;
    public int heat;
    public int maxHeat;
    public int currentEnergyGeneration = 0;
    public boolean isOn;
    public TileReactor() {
        heat = 0;
        maxHeat = LibReactor.DEFAULT_MAX_HEAT;
        reactorHandler = new ReactorHandler(LibReactor.DEFAULT_MAX_HEAT, columns, COLUMN_LENGTH);
        setupItemStackHandler();
        energyHandler = new EnergyHandler(0, 0, 1000000, false, true);
        energyHandler.setHoldsEnergy(false);
        isOn = false;
    }

    public void setupItemStackHandler () {
        //itemStackHandler = new ItemStackHandler (columns*COLUMN_LENGTH) {
        itemStackHandler = new ItemStackHandler(getNumSlots()){ // TODO change
          @Override
          protected void onContentsChanged (int slot) {
              // TODO potentially add to
              TileReactor.this.markDirty();
              reactorHandler.updateItems(this);

          }
        };
    }

    public boolean canInteractWith (EntityPlayer player) {
        return !isInvalid() && player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= REACH_DIST;
    }

    public void readFromNBT (NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("reactor")) {
            CapabilityReactor.REACTOR_HANDLER.readNBT(reactorHandler, null, compound.getTag("reactor"));
        }
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
            reactorHandler.updateItems(itemStackHandler);
        }
        // TODO add energy
    }

    public NBTTagCompound writeToNBT (NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setTag("reactor", CapabilityReactor.REACTOR_HANDLER.writeNBT(reactorHandler, null));
        return compound;
        // TODO add energy
    }


    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        //System.out.println("Getting capability");
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        } else if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyHandler);
        } else if (capability == CapabilityReactor.REACTOR_HANDLER) {
            return CapabilityReactor.REACTOR_HANDLER.cast(reactorHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        else if (capability == CapabilityEnergy.ENERGY) {
            return true;
        } else if (capability == CapabilityReactor.REACTOR_HANDLER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    public void update () {
        if (world.isRemote) {
            return;
        }
        if (isOn != reactorHandler.isActive()) {
            reactorHandler.setActive(isOn);
        }
        reactorHandler.update();
        giveEnergy(this.world, this.pos, false, reactorHandler.getEnergyProduction());
    }
    public int getNumSlots () {
        return columns * COLUMN_LENGTH;
    }


    private void giveEnergy (World world, BlockPos pos, boolean simulate, int energy) {
        HashMap<EnumFacing, TileEntity> tiles = new HashMap<>();
        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity te = world.getTileEntity(pos.offset(side));
            if (te == null) {
                continue;
            } else if (te.hasCapability(CapabilityEnergy.ENERGY, side)) {
                tiles.put(side, te);
            }
        }
        if (tiles.size() <= 0) {
            return;
        }
        int energyPerSide = energy / tiles.size();
        int extraEnergy = 0;
        Iterator<Map.Entry<EnumFacing, TileEntity>> tilesIterator = tiles.entrySet().iterator();
        while (tilesIterator.hasNext()) {
            Map.Entry<EnumFacing, TileEntity> entry = tilesIterator.next();
            EnumFacing side = entry.getKey();
            TileEntity te = entry.getValue();
            int energyGiven = te.getCapability(CapabilityEnergy.ENERGY, side)
                                .receiveEnergy(energyPerSide, simulate);
            if (energyGiven < energyPerSide) {
                extraEnergy += energyPerSide - energyGiven;
            }
        }

    }
}
