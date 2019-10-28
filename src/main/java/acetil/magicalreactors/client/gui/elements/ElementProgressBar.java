package acetil.magicalreactors.client.gui.elements;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

public class ElementProgressBar extends ElementPercentBar {
    @Override
    public IGuiElement applyJson (GuiElementJson json) {
        super.applyJson(json);
        setDirection(BarDirection.RIGHT);
        setProgressFunction((GuiContainer gui, IMachineCapability machineHandler, IItemHandler itemHandler, IEnergyStorage energyHandler,
                             IFluidHandler fluidHandler) -> {
            float progress = 0;
            if (machineHandler.energyRequired() != 0) {
                progress = 1.0f - (float)machineHandler.energyToCompletion() / machineHandler.energyRequired();
            }
            return progress;
        });
        return this;
    }
}
