package acetil.magicalreactors.client.gui.elements;

import acetil.magicalreactors.client.gui.ContainerGui;
import acetil.magicalreactors.common.capabilities.EnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
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
        setProgressFunction((ContainerGui gui, TileEntity te) -> {
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, null).orElse(CapabilityEnergy.ENERGY.getDefaultInstance());
            return (float)energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
        });
        return this;
    }
}
