package acetil.magicalreactors.common.capabilities.reactor;

import acetil.magicalreactors.common.reactor.ReactorCoolingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ReactorCoolingInterface implements IReactorInterfaceHandler {
    private ItemStackHandler itemHandler;
    public ReactorCoolingInterface (ItemStackHandler itemHandler) {
        this.itemHandler = itemHandler;
    }
    @Override
    public void updateInterface(IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl) {
        int cooling = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i) != ItemStack.EMPTY) {
                int itemAmount = itemHandler.getStackInSlot(i).getCount();
                int coolingPerItem = ReactorCoolingRegistry.getCooling(itemHandler.getStackInSlot(i).getItem().getRegistryName().toString());
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                cooling += itemAmount * coolingPerItem;
            }
        }
        reactorHandler.cool(cooling);
    }
}
