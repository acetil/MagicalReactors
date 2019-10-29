package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.capabilities.CapabilityReactorFuel;
import acetil.magicalreactors.common.reactor.IReactorFuel;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class ReactorFuelInputInterface implements IReactorInterfaceHandler {
    private ItemStackHandler itemHandler;
    public ReactorFuelInputInterface (ItemStackHandler itemHandler) {
        this.itemHandler = itemHandler;
    }
    @Override
    public void updateInterface(IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl) {

    }
    @Override
    public List<IReactorFuel> getReactorFuels () {
        List<IReactorFuel> fuels = new ArrayList<>();
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            fuels.add(itemHandler.getStackInSlot(0).getCapability(CapabilityReactorFuel.FUEL_CAPABILITY, null));
            itemHandler.getStackInSlot(0).shrink(1);
        }
        return fuels;
    }
}
