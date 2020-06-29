package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.client.gui.data.GuiDataManager;
import acetil.magicalreactors.common.capabilities.CapabilityMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import acetil.magicalreactors.client.gui.json.GuiElementJson;
import acetil.magicalreactors.common.capabilities.machines.machinehandlers.IMachineCapability;

import java.util.function.Function;

public class ElementProgressBar extends ElementPercentBar {
    @Override
    @SuppressWarnings("unchecked")
    public IGuiElement applyJson (GuiElementJson json) {
        super.applyJson(json);
        setDirection(BarDirection.RIGHT);
        setProgressFunction((ContainerGui gui, TileEntity te) ->
                ((Function<TileEntity, Float>) GuiDataManager.getVariable("machine.fractionComplete")).apply(te));
        return this;
    }
}
