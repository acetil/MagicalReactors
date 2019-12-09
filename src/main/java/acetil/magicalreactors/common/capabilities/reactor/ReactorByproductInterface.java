package acetil.magicalreactors.common.capabilities.reactor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ReactorByproductInterface implements IReactorInterfaceHandler{
    private ItemStackHandler itemHandler;
    public ReactorByproductInterface (ItemStackHandler itemHandler) {
        this.itemHandler = itemHandler;
    }
    @Override
    public void updateInterface(IReactorHandlerNew reactorHandler, IReactorControlCapability reactorControl) {
        ItemStack stack = reactorHandler.getNextOutput(true);
        while (!stack.isEmpty() && canInsertStack(stack)) {
            System.out.println("Extracting byproduct!");
            insertStack(reactorHandler.getNextOutput(false));
            stack = reactorHandler.getNextOutput(true);
        }
    }
    private boolean canInsertStack (ItemStack stack) {
        ItemStack stackTemp = stack;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            stackTemp = itemHandler.insertItem(i, stackTemp, true);
            if (stackTemp.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    private void insertStack (ItemStack stack) {
        ItemStack stackTemp = stack;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            stackTemp = itemHandler.insertItem(i, stackTemp, false);
            if (stackTemp.isEmpty()) {
                break;
            }
        }
    }
}
