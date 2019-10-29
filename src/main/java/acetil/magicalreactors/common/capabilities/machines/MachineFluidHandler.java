package acetil.magicalreactors.common.capabilities.machines;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MachineFluidHandler implements IFluidHandler {
    private int inputSlots;
    private int outputSlots;
    private int fluidCapacity;
    private List<FluidStack> fluidStacks;
    public MachineFluidHandler (int inputSlots, int outputSlots, int fluidCapacity) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        fluidStacks = new ArrayList<>();
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            fluidStacks.add(null);
        }
        this.fluidCapacity = fluidCapacity;
    }
    @Override
    public IFluidTankProperties[] getTankProperties() {
        //System.out.println("Attempted to get tank properties");
        IFluidTankProperties[] properties = new IFluidTankProperties[inputSlots + outputSlots];
        for (int i = 0; i < fluidStacks.size(); i++) {
            properties[i] = new FluidProperties(i < inputSlots, fluidStacks.get(i), fluidCapacity);
        }
        return properties;
    }
    public FluidStack getContents (int slot) {
        return fluidStacks.get(slot);
    }
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        //System.out.println("Attempted fill");
        for (int i = 0; i < inputSlots; i++) {
            if (fluidStacks.get(i) == null) {
                int amountFilled = Math.min(resource.amount, fluidCapacity);
                if (doFill) {
                    fluidStacks.remove(i);
                    fluidStacks.add(i, new FluidStack(resource.getFluid(), amountFilled, resource.tag));
                }
                //System.out.println("Filled " + amountFilled + " into an empty slot!");
                onContentsChanged(i, amountFilled);
                return amountFilled;
            } else if (resource.isFluidEqual(fluidStacks.get(i))) {
                int amountFilled = Math.min(resource.amount, fluidCapacity - fluidStacks.get(i).amount);
                if (doFill) {
                    fluidStacks.get(i).amount += amountFilled;
                }
                //System.out.println("Filled " + amountFilled + " into a slot with fluid in it!");
                onContentsChanged(i, amountFilled);
                return amountFilled;
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        for (int i = inputSlots; i < fluidStacks.size(); i++) {
            if (fluidStacks.get(i) != null && resource.isFluidEqual(fluidStacks.get(i))) {
                int amountDrained = Math.min(resource.amount, fluidStacks.get(i).amount);
                if (doDrain) {
                    fluidStacks.get(i).amount -= amountDrained;
                    if (fluidStacks.get(i).amount <= 0) {
                        fluidStacks.remove(i);
                        fluidStacks.add(i, null);
                    }
                    onContentsChanged(i, amountDrained * -1);
                }
                return new FluidStack(resource.getFluid(), amountDrained, resource.tag);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        for (int i = inputSlots; i < fluidStacks.size(); i++) {
            if (fluidStacks.get(i) != null) {
                int amountDrained = Math.min(maxDrain, fluidStacks.get(i).amount);
                Fluid fluidType = fluidStacks.get(i).getFluid();
                CompoundNBT tag = fluidStacks.get(i).tag;
                if (doDrain) {
                    fluidStacks.get(i).amount -= amountDrained;
                    if (fluidStacks.get(i).amount <= 0) {

                        fluidStacks.remove(i);
                        fluidStacks.add(i, null);
                    }
                    onContentsChanged(i, amountDrained * -1);
                }
                return new FluidStack(fluidType, amountDrained, tag);
            }
        }
        return null;
    }
    public int addFluid (FluidStack resource, int slot, boolean simulate) {
        if (fluidStacks.get(slot) == null) {
            int amountAdded = Math.min(resource.amount, fluidCapacity);
            fluidStacks.remove(slot);
            fluidStacks.add(slot, new FluidStack(resource.getFluid(), amountAdded, resource.tag));
            onContentsChanged(slot, amountAdded);
            return amountAdded;
        }
        if (resource.isFluidEqual(fluidStacks.get(slot))) {
            int amountAdded = Math.min(resource.amount, fluidCapacity - fluidStacks.get(slot).amount);
            if (!simulate) {
                fluidStacks.get(slot).amount += amountAdded;
            }
            onContentsChanged(slot, amountAdded);
            return amountAdded;
        }
        return 0;
    }
    public int removeFluid (int amount, int slot, boolean simulate) {
        if (fluidStacks.get(slot) == null) {
            return 0;
        }
        int amountRemoved = Math.min(amount, fluidStacks.get(slot).amount);
        if (!simulate) {
            fluidStacks.get(slot).amount -= amountRemoved;
            if (fluidStacks.get(slot).amount <= 0) {
                fluidStacks.remove(slot);
                fluidStacks.add(slot, null);
                onContentsChanged(slot, amountRemoved * -1);
            }
            return amountRemoved;
        }
        return 0;
    }
    public FluidStack getFluid (int slot) {
        return fluidStacks.get(slot);
    }
    public void readNBT (CompoundNBT nbt) {
        if (nbt.getInt("num_inputs") != inputSlots && nbt.getInt("num_outputs") != outputSlots) {
            throw new IllegalArgumentException("Wrong number of inputs or outputs, probably a different tile entity!");
        }
        fluidStacks.clear();
        CompoundNBT fluids = nbt.getCompound("fluids");
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            CompoundNBT fluidTag = fluids.getCompound("fluid" + i);
            fluidStacks.add(new FluidStack(FluidRegistry.getFluid(fluidTag.getString("name")),
                    fluidTag.getInt("amount")));
            if (fluidTag.contains("nbt")) {
                fluidStacks.get(i).tag = fluidTag.getCompound("tag");
            }
        }
    }
    public CompoundNBT writeNBT () {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("num_inputs", inputSlots + outputSlots);
        CompoundNBT fluids = new CompoundNBT();
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            FluidStack stack = fluidStacks.get(i);
            if (stack == null) {
                continue;
            }
            CompoundNBT fluidTag = new CompoundNBT();
            fluidTag.putString("name", stack.getFluid().getRegistryName().toString());
            fluidTag.putInt("amount", stack.getAmount());
            if (stack.getTag() != null) {
                fluidTag.put("nbt", stack.getTag());
            }
            fluids.put("fluid" + i, fluidTag);
        }
        nbt.put("fluids", fluids);
        return nbt;
    }
    protected void onContentsChanged (int slot, int amount) {

    }
}
class FluidProperties implements IFluidTankProperties {
    public boolean isInput;
    public boolean isOutput;
    FluidStack fluidStack;
    int capacity;
    Predicate<FluidStack> canFill;
    Predicate<FluidStack> canDrain;
    FluidProperties (boolean isInput, FluidStack fluidStack, int capacity, Predicate<FluidStack> canFill, Predicate<FluidStack> canDrain) {
        this.isInput = isInput;
        this.isOutput = !isInput;
        this.fluidStack = fluidStack;
        this.capacity = capacity;
        this.canFill = canFill;
        this.canDrain = canDrain;
    }
    FluidProperties (boolean isInput, FluidStack fluidStack, int capacity) {
        this(isInput, fluidStack, capacity, null, null);
    }

    @Nullable
    @Override
    public FluidStack getContents() {
        return fluidStack;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean canFill() {
        return isInput;
    }

    @Override
    public boolean canDrain() {
        return isOutput ;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        if (canFill == null) {
            return true;
        }
        return canFill.test(fluidStack);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        if (canDrain == null) {
            return true;
        }
        return canDrain.test(fluidStack);
    }
}
