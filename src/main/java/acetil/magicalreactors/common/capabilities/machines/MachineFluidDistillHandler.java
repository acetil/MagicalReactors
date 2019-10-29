package acetil.magicalreactors.common.capabilities.machines;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachineFluidDistillHandler implements IFluidHandler {
    private MachineFluidHandler fluidHandler;
    private int bottomSlots;
    private boolean isBottom;
    public MachineFluidDistillHandler (MachineFluidHandler fluidHandler, int bottomSlots, boolean isBottom) {
            this.fluidHandler = fluidHandler;
            this.bottomSlots = bottomSlots;
            this.isBottom = isBottom;
    }

    @Override
    public int getTanks() {
        if (isBottom) {
            return bottomSlots;
        } else {
            return fluidHandler.getTanks()  - bottomSlots;
        }
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (isBottom) {
            return fluidHandler.getFluidInTank(tank);
        }
        return fluidHandler.getFluidInTank(bottomSlots + tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        if (isBottom) {
            return fluidHandler.getTankCapacity(tank);
        } else {
            return fluidHandler.getTankCapacity(bottomSlots + tank);
        }
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (isBottom) {
            return fluidHandler.isFluidValid(tank, stack);
        } else {
            return fluidHandler.isFluidValid(tank + bottomSlots, stack);
        }
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!isBottom) {
            return 0;
        }
        FluidStack resourceTemp = resource.copy();
        int amountFilled = 0;
        boolean simulate = action.simulate();
        for (int i = 0; i < fluidHandler.getInputSlots(); i++) {
            int newAmountFilled = fluidHandler.addFluid(resourceTemp, i, simulate);
            resourceTemp.shrink(newAmountFilled);
            amountFilled += newAmountFilled;
        }
        return amountFilled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int start = 0;
        int end = 0;
        if (isBottom) {
            start = fluidHandler.getInputSlots();
            end = bottomSlots;
        } else {
            start = bottomSlots;
            end = fluidHandler.getOutputSlots();
        }
        FluidStack output = new FluidStack(resource.getFluid(), 0, resource.getTag());
        int amountToDrain = resource.getAmount();
        boolean simulate = action.simulate();
        for (int i = start; i < end; i++) {
            if (getFluidInTank(i).isFluidEqual(output)) {
                int previousOutputAmount = output.getAmount();
                output.grow(fluidHandler.removeFluid(amountToDrain, i, simulate));
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
        int slot = 0;
        int end = 0;
        if (isBottom) {
            slot = fluidHandler.getInputSlots();
            end = bottomSlots;
        } else {
            slot = bottomSlots;
            end = fluidHandler.getTanks();
        }
        while (targetedStack.isEmpty() && slot < end) {
            slot++;
            targetedStack = fluidHandler.getFluid(slot);
        }
        if (targetedStack.isEmpty()) {
            return FluidStack.EMPTY;
        }
        return drain(new FluidStack(targetedStack.getFluid(), maxDrain, targetedStack.getTag()), action);
    }
}
