package acetil.magicalreactors.common.capabilities.machines;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MachineFluidHandler implements IFluidHandler {
    private int inputSlots;
    private int outputSlots;
    private int fluidCapacity;
    private FluidStack[] fluidStacks;
    public MachineFluidHandler (int inputSlots, int outputSlots, int fluidCapacity) {
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        fluidStacks = new FluidStack[inputSlots + outputSlots];
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            fluidStacks[i] = FluidStack.EMPTY;
    }
        this.fluidCapacity = fluidCapacity;
    }
    public int addFluid (FluidStack resource, int slot, boolean simulate) {
        if (fluidStacks[slot] == FluidStack.EMPTY) {
            int amountAdded = Math.min(resource.getAmount(), fluidCapacity);
            if (!simulate) {
                fluidStacks[slot] = new FluidStack(resource.getFluid(), amountAdded, resource.getTag());
                onContentsChanged(slot, amountAdded);
            }
            return amountAdded;
        }
        if (resource.isFluidEqual(fluidStacks[slot])) {
            int amountAdded = Math.min(resource.getAmount(), fluidCapacity - fluidStacks[slot].getAmount());
            if (!simulate) {
                fluidStacks[slot].grow(amountAdded);
                onContentsChanged(slot, amountAdded);
            }
            return amountAdded;
        }
        return 0;
    }
    public int removeFluid (int amount, int slot, boolean simulate) {
        if (fluidStacks[slot] == FluidStack.EMPTY) {
            return 0;
        }
        int amountRemoved = Math.min(amount, fluidStacks[slot].getAmount());
        if (!simulate) {
            fluidStacks[slot].shrink(amountRemoved);
            if (fluidStacks[slot].getAmount() <= 0) {
                fluidStacks[slot] = FluidStack.EMPTY;
                onContentsChanged(slot, amountRemoved * -1);
            }

        }
        return amountRemoved;
    }
    public FluidStack getFluid (int slot) {
        return fluidStacks[slot];
    }
    public void readNBT (CompoundNBT nbt) {
        if (nbt.getInt("num_inputs") != inputSlots && nbt.getInt("num_outputs") != outputSlots) {
            throw new IllegalArgumentException("Wrong number of inputs or outputs, probably a different tile entity!");
        }
        CompoundNBT fluids = nbt.getCompound("fluids");
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            if (fluids.contains("fluid" + i)) {
                CompoundNBT fluidTag = fluids.getCompound("fluid" + i);
                fluidStacks[i] = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidTag.getString("name"))),
                        fluidTag.getInt("amount"));
                if (fluidTag.contains("nbt")) {
                    fluidStacks[i].setTag(fluidTag.getCompound("nbt"));
                }
            }
        }
    }
    public CompoundNBT writeNBT () {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("num_inputs", inputSlots + outputSlots);
        CompoundNBT fluids = new CompoundNBT();
        for (int i = 0; i < inputSlots + outputSlots; i++) {
            FluidStack stack = fluidStacks[i];
            if (stack == FluidStack.EMPTY) {
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

    @Override
    public int getTanks() {
        return inputSlots + outputSlots;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidStacks[tank];
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidCapacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true; //TODO: update
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        FluidStack resourceTemp = resource.copy();
        int amountFilled = 0;
        boolean simulate = action.simulate();
        for (int i = 0; i < inputSlots; i++) {
            int newAmountFilled = addFluid(resourceTemp, i, simulate);
            resourceTemp.shrink(newAmountFilled);
            amountFilled += newAmountFilled;
        }
        return amountFilled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack output = new FluidStack(resource.getFluid(), 0, resource.getTag());
        int amountToDrain = resource.getAmount();
        boolean simulate = action.simulate();
        for (int i = inputSlots; i < inputSlots + outputSlots; i++) {
            if (getFluidInTank(i).isFluidEqual(output)) {
                int previousOutputAmount = output.getAmount();
                output.grow(removeFluid(amountToDrain, i, simulate));
                amountToDrain -= output.getAmount() - previousOutputAmount;
            }
        }
        if (output.isEmpty()) {
            return FluidStack.EMPTY;
        }
        return output;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack targetedStack = FluidStack.EMPTY;
        int slot = inputSlots - 1;
        while (targetedStack.isEmpty() && slot < inputSlots + outputSlots) {
            slot++;
            targetedStack = fluidStacks[slot];
        }
        if (targetedStack.isEmpty()) {
            return FluidStack.EMPTY;
        }
        return drain(new FluidStack(targetedStack.getFluid(), maxDrain, targetedStack.getTag()), action);
    }
    public int getInputSlots () {
        return inputSlots;
    }
    public int getOutputSlots () {
        return outputSlots;
    }
}
