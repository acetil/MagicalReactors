package acetil.magicalreactors.common.capabilities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MachineGuiItemHandler implements IItemHandler {
    private ItemStackHandler itemHandler;
    private int inputSlots;
    private int outputSlots;
    public MachineGuiItemHandler (ItemStackHandler itemHandler, int inputSlots, int outputSlots) {
        this.itemHandler = itemHandler;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot < inputSlots) {
            return itemHandler.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack); //TODO update
    }

    public int getInputSlots() {
        return inputSlots;
    }

    public int getOutputSlots() {
        return outputSlots;
    }
}
