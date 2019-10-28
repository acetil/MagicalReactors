package acetil.magicalreactors.common.capabilities.machines;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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
    public IFluidTankProperties[] getTankProperties() {
        IFluidTankProperties[] propertiesI = fluidHandler.getTankProperties();
        FluidProperties[] properties = new FluidProperties[propertiesI.length];
        for (int i = 0; i < propertiesI.length; i++) {
            properties[i] = (FluidProperties) propertiesI[i];
            properties[i].isInput &= isBottom; // inputs only occur on bottom tile
            properties[i].isOutput &= (isBottom && i < bottomSlots) || (!isBottom && i >= bottomSlots);
        }
        return properties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (isBottom) {
            return fluidHandler.fill(resource, doFill);
        }
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        IFluidTankProperties[] properties = fluidHandler.getTankProperties();
        if (isBottom) {
            for (int i = 0; i < bottomSlots; i++) {
                if (properties[i].canDrain() && fluidHandler.getFluid(i).isFluidEqual(resource)) {
                    return fluidHandler.drain(resource, doDrain);
                }
            }
        } else {
            for (int i = 0; i < bottomSlots; i++) {
                if (properties[i].canDrain() && fluidHandler.getFluid(i).isFluidEqual(resource)) {
                    return fluidHandler.drain(resource, doDrain);
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        IFluidTankProperties[] properties = getTankProperties();
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].canDrain()) {
                FluidStack fluid = fluidHandler.getFluid(i);
                int amount = fluidHandler.removeFluid(maxDrain, i, !doDrain);
                return new FluidStack(fluid.getFluid(), amount, fluid.tag);
            }
        }
        return null;
    }
}
