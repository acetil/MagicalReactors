package acetil.magicalreactors.common.capabilities.machines;

import acetil.magicalreactors.common.recipes.MachineRecipeManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MachineGuiItemHandler implements IItemHandler, IItemHandlerModifiable {
    private ItemStackHandler itemHandler;
    private int inputSlots;
    private String machine;
    public MachineGuiItemHandler (String machine, ItemStackHandler itemHandler, int inputSlots) {
        this.itemHandler = itemHandler;
        this.inputSlots = inputSlots;
        this.machine = machine;
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
        if (slot < inputSlots && isItemValid(slot, stack)) {
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
        if (slot < inputSlots) {
            return MachineRecipeManager.isValidInput(machine, stack.getItem());
        } else {
            return false;
        }
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

}
