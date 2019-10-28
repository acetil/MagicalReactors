package acetil.magicalreactors.client.gui.elements;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

public class ElementEnergyStorage extends ElementProgressBar {
    @Override
    public IGuiElement applyJson (GuiElementJson json) {
        super.applyJson(json);
        setDirection(BarDirection.UP);
        setProgressFunction((GuiContainer gui, IMachineCapability machineHandler, IItemHandler itemHandler, IEnergyStorage energyHandler,
                IFluidHandler fluidHandler) -> (float)energyHandler.getEnergyStored() / energyHandler.getMaxEnergyStored());
        return this;
    }
}
