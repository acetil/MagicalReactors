package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.data.GuiDataManager;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

import java.util.function.Function;

public class ElementEnergyStorage extends ElementProgressBar {

    @SuppressWarnings("unchecked")
    @Override
    public IGuiElement applyJson (GuiElementJson json) {
        super.applyJson(json);
        setDirection(BarDirection.UP);
        Function<TileEntity, ?> filledFunc = GuiDataManager.getVariable("energy.fractionStored");
        setProgressFunction((ContainerGui gui, TileEntity te) ->
                ((Function<TileEntity, Float>) filledFunc).apply(te));
        return this;
    }
}
